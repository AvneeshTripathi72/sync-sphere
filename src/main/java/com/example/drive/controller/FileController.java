package com.example.drive.controller;

import com.example.drive.dto.CreateFolderRequest;
import com.example.drive.dto.FileResponse;
import com.example.drive.entity.FileEntity;
import com.example.drive.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST controller for file upload, folder creation, listing, download, and deletion.
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "parentFolderId", required = false) Long parentFolderId) throws IOException {
        FileResponse saved = fileStorageService.saveFile(file, parentFolderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/folders")
    public ResponseEntity<FileResponse> createFolder(@RequestBody CreateFolderRequest request) {
        FileResponse folder = fileStorageService.createFolder(
                request != null ? request.getName() : null,
                request != null ? request.getParentFolderId() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(folder);
    }

    @GetMapping("/downloads/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long fileId) {
        FileEntity metadata = fileStorageService.getFileEntityById(fileId);
        Resource resource = fileStorageService.loadFileAsResource(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getName() + "\"")
                .body(resource);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> getFilesInFolder(
            @RequestParam(value = "parentFolderId", required = false) Long parentFolderId) {
        return ResponseEntity.ok(fileStorageService.getFilesInFolder(parentFolderId));
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable Long fileId) throws IOException {
        fileStorageService.deleteFile(fileId);
        return ResponseEntity.ok(Collections.singletonMap("message", "File deleted successfully"));
    }
}
