package com.example.drive.service;

import com.example.drive.config.StorageProperties;
import com.example.drive.dto.FileResponse;
import com.example.drive.entity.FileEntity;
import com.example.drive.exception.BadRequestException;
import com.example.drive.exception.ResourceNotFoundException;
import com.example.drive.repo.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Manages disk storage and metadata persistence for uploaded files and folders.
 */
@Service
public class FileStorageService {

    public static final String TYPE_FILE = "file";
    public static final String TYPE_FOLDER = "folder";

    private final Path uploadRoot;
    private final FileRepository fileRepository;

    public FileStorageService(StorageProperties storageProperties, FileRepository fileRepository) {
        this.uploadRoot = Paths.get(storageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
    }

    public FileResponse saveFile(MultipartFile file, Long parentFolderId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded file must not be empty");
        }
        validateParentFolder(parentFolderId);

        Files.createDirectories(uploadRoot);
        String originalName = sanitizeFileName(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "_" + originalName;
        Path targetPath = resolveSafePath(storedName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity entity = new FileEntity();
        entity.setName(originalName);
        entity.setPath(targetPath.toString());
        entity.setType(TYPE_FILE);
        entity.setSize(file.getSize());
        entity.setParentFolderId(parentFolderId);
        entity.setCreatedAt(LocalDateTime.now());

        return FileResponse.fromEntity(fileRepository.save(entity));
    }

    public FileResponse createFolder(String folderName, Long parentFolderId) {
        if (folderName == null || folderName.trim().isEmpty()) {
            throw new BadRequestException("Folder name is required");
        }
        validateParentFolder(parentFolderId);

        FileEntity folder = new FileEntity();
        folder.setName(folderName.trim());
        folder.setType(TYPE_FOLDER);
        folder.setSize(0L);
        folder.setParentFolderId(parentFolderId);
        folder.setCreatedAt(LocalDateTime.now());

        return FileResponse.fromEntity(fileRepository.save(folder));
    }

    public List<FileResponse> getFilesInFolder(Long parentFolderId) {
        List<FileEntity> entities = (parentFolderId == null)
                ? fileRepository.findByParentFolderIdIsNull()
                : fileRepository.findByParentFolderId(parentFolderId);

        return entities.stream().map(FileResponse::fromEntity).toList();
    }

    public FileEntity getFileEntityById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with ID: " + fileId));
    }

    public Resource loadFileAsResource(Long fileId) {
        FileEntity entity = getFileEntityById(fileId);
        if (TYPE_FOLDER.equals(entity.getType()) || entity.getPath() == null) {
            throw new BadRequestException("Folders cannot be downloaded as files");
        }

        try {
            Path filePath = Paths.get(entity.getPath()).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Stored file not found on disk: " + entity.getName());
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Invalid file path for ID: " + fileId);
        }
    }

    public void deleteFile(Long fileId) throws IOException {
        FileEntity entity = getFileEntityById(fileId);
        if (TYPE_FOLDER.equals(entity.getType())) {
            List<FileEntity> children = fileRepository.findByParentFolderId(fileId);
            if (!children.isEmpty()) {
                throw new BadRequestException("Cannot delete non-empty folder");
            }
        } else if (entity.getPath() != null) {
            Files.deleteIfExists(Paths.get(entity.getPath()));
        }
        fileRepository.deleteById(fileId);
    }

    private void validateParentFolder(Long parentFolderId) {
        if (parentFolderId == null) {
            return;
        }
        FileEntity parent = getFileEntityById(parentFolderId);
        if (!TYPE_FOLDER.equals(parent.getType())) {
            throw new BadRequestException("Parent ID does not refer to a folder");
        }
    }

    private String sanitizeFileName(String rawName) {
        String cleaned = StringUtils.cleanPath(rawName == null ? "unnamed" : rawName);
        String fileNameOnly = Paths.get(cleaned).getFileName().toString();
        if (fileNameOnly.isEmpty() || fileNameOnly.contains("..")) {
            throw new BadRequestException("Invalid file name");
        }
        return fileNameOnly;
    }

    private Path resolveSafePath(String storedName) {
        Path target = uploadRoot.resolve(storedName).normalize();
        if (!target.startsWith(uploadRoot)) {
            throw new BadRequestException("Invalid storage path");
        }
        return target;
    }
}
