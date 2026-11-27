package com.example.drive.repo;

import com.example.drive.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link FileEntity} records.
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findByParentFolderId(Long parentFolderId);

    List<FileEntity> findByParentFolderIdIsNull();
}
