package com.example.drive.repo;

import com.example.drive.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface fileRepository extends JpaRepository<FileEntity, Long> {

    // Custom method to find by parent folder ID
    List<FileEntity> findByParentFolderTime(Long parentFolderId);
}

