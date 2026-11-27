package com.example.drive.dto;

/**
 * Request payload for creating a virtual folder.
 */
public class CreateFolderRequest {

    private String name;
    private Long parentFolderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
}
