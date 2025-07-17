package com.example.drive.controler;

import com.example.drive.entity.FileEntity;
import com.example.drive.services.FileServiceStorage;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    private final FileServiceStorage fileServiceStorage;
    public FileController(FileServiceStorage fileServiceStorage, FileServiceStorage fileServiceStorage1) {
        this.fileServiceStorage = fileServiceStorage1;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam(value = "parentFolderId", required = false) Long parentFolderId ) {
       // return ResponseEntity.ok("File uploaded successfully");
        try{
            String response = fileServiceStorage.saveFile(file,parentFolderId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/downloads/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try{
            FileEntity fileEntity = fileServiceStorage.getFilesInFolder(Long.valueOf(fileId)).get(0);
            Path path = Paths.get(fileEntity.getPath());
            UrlResource resource = new UrlResource(path.toUri());
            return (ResponseEntity<Resource>) ResponseEntity.ok().header("content-Dispositjion","attachment,filename = \""+ fileEntity.getName());
        } catch (MalformedURLException e) {
            return ResponseEntity.status(484).build();
    }

    }
    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> getFilesInFolder(@RequestParam(value = "parentFolderId", required = false) Long parentFolderId) {
    List<FileEntity> files;
    if(parentFolderId == null){
        files = fileServiceStorage.getFilesInFolder(null);
    } else {
        files = fileServiceStorage.getFilesInFolder(parentFolderId);
    }
    return ResponseEntity.ok(files);
    }
   // @DeleteMapping("/delete/{fileid}")



    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        try {
            FileEntity fileEntity = fileServiceStorage.getFileById(fileId); // Use a proper method here
            Path path = Paths.get(fileEntity.getPath());
            Files.deleteIfExists(path);

            fileServiceStorage.deleteFile(fileId);
            return ResponseEntity.ok("File deleted successfully");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to delete file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



}
