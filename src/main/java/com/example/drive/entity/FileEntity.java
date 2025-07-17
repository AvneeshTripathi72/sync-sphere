package com.example.drive.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    private String type;
    private Long size;
    private Long parentFolderTime;

    private LocalDateTime createdAt;

    public FileEntity() {
    }

    public FileEntity(Long id, String name, String path, String type, Long size, Long parentFolderTime, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
        this.parentFolderTime = parentFolderTime;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getParentFolderTime() {
        return parentFolderTime;
    }

    public void setParentFolderTime(Long parentFolderTime) {
        this.parentFolderTime = parentFolderTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
