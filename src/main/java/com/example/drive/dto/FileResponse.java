package com.example.drive.dto;

import com.example.drive.entity.FileEntity;

import java.time.LocalDateTime;

/**
 * API response DTO representing a stored file or folder without exposing internal disk paths.
 */
public class FileResponse {

    private final Long id;
    private final String name;
    private final String type;
    private final Long size;
    private final Long parentFolderId;
    private final LocalDateTime createdAt;

    public FileResponse(Long id, String name, String type, Long size,
                        Long parentFolderId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.parentFolderId = parentFolderId;
        this.createdAt = createdAt;
    }

    public static FileResponse fromEntity(FileEntity entity) {
        return new FileResponse(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getSize(),
                entity.getParentFolderId(),
                entity.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Long getSize() {
        return size;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
