package com.example.drive.service;

import com.example.drive.config.StorageProperties;
import com.example.drive.dto.FileResponse;
import com.example.drive.entity.FileEntity;
import com.example.drive.exception.BadRequestException;
import com.example.drive.repo.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private FileRepository fileRepository;

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        StorageProperties props = new StorageProperties();
        props.setUploadDir(tempDir.toString());
        fileStorageService = new FileStorageService(props, fileRepository);
    }

    @Test
    void saveFile_persistsFileAndSize() throws Exception {
        MockMultipartFile multipart = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "hello syncsphere".getBytes(StandardCharsets.UTF_8));

        when(fileRepository.save(any(FileEntity.class))).thenAnswer(inv -> {
            FileEntity entity = inv.getArgument(0);
            entity.setId(42L);
            return entity;
        });

        FileResponse response = fileStorageService.saveFile(multipart, null);
        assertEquals(42L, response.getId());
        assertEquals("notes.txt", response.getName());
        assertEquals(FileStorageService.TYPE_FILE, response.getType());
        assertEquals(16L, response.getSize());
    }

    @Test
    void saveFile_rejectsEmptyFile() {
        MockMultipartFile empty = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);
        assertThrows(BadRequestException.class, () -> fileStorageService.saveFile(empty, null));
    }

    @Test
    void loadFileAsResource_loadsSavedFile() throws Exception {
        MockMultipartFile multipart = new MockMultipartFile(
                "file",
                "report.txt",
                "text/plain",
                "data".getBytes(StandardCharsets.UTF_8));

        final FileEntity[] captured = new FileEntity[1];
        when(fileRepository.save(any(FileEntity.class))).thenAnswer(inv -> {
            FileEntity entity = inv.getArgument(0);
            entity.setId(7L);
            captured[0] = entity;
            return entity;
        });

        fileStorageService.saveFile(multipart, null);
        when(fileRepository.findById(7L)).thenReturn(Optional.of(captured[0]));

        Resource resource = fileStorageService.loadFileAsResource(7L);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }
}
