package com.example.drive.services;

import com.example.drive.entity.FileEntity;
import com.example.drive.repo.fileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class FileServiceStorage {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final fileRepository fileRepository;

    public FileServiceStorage(fileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFile(MultipartFile file, Long parentFolderId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setPath(filePath.toString());
        fileEntity.setType("file");
        fileEntity.setParentFolderTime(parentFolderId);
        fileEntity.setCreatedAt(java.time.LocalDateTime.now());

        fileRepository.save(fileEntity);
        return "File uploaded successfully";
    }

    public List<FileEntity> getFilesInFolder(Long parentFolderId) {
        if (parentFolderId == null) {
            return fileRepository.findByParentFolderTime(null);
        } else {
            return fileRepository.findByParentFolderTime(parentFolderId);
        }
    }
    public void deleteFile(Long fileId) {
        fileRepository.deleteById(fileId);
    }
    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));
    }
}
