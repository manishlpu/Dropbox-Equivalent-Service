package com.example.springapi.api.repository;

import com.example.springapi.api.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileDB, String> {

}
