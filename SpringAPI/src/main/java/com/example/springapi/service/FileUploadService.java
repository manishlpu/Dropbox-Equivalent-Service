package com.example.springapi.service;

import com.example.springapi.api.model.FileDB;
import com.example.springapi.api.repository.FileMetadataRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class FileUploadService {

    String id = "";
    Date date = new Date();
    @Autowired
    private FileMetadataRepository fileMetadataRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public FileDB store(MultipartFile file) throws IOException {
        // TODO: Implement exception handling
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB();
        fileDB.setName(file.getOriginalFilename());
        fileDB.setSize(file.getSize());
        fileDB.setData(file.getBytes());
        fileDB.setTime(date.getTime());
        fileDB.setType(file.getContentType());
        entityManager.persist(fileDB);
        id = fileDB.getId();
        return fileMetadataRepository.save(fileDB);
    }

    public String getId() {
        return id;
    }

    public FileDB getFile(String id) {
        // TODO: Implement exception handling
        return fileMetadataRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        // TODO: Implement exception handling
        return fileMetadataRepository.findAll().stream();
    }

    public FileDB updateFile(MultipartFile file, String fileId) throws IOException {
        // TODO: Implement exception handling
        FileDB fileDB;
        fileDB = fileMetadataRepository.findById(fileId).get();
        fileDB.setName(file.getOriginalFilename());
        fileDB.setSize(file.getSize());
        fileDB.setData(file.getBytes());
        fileDB.setTime(date.getTime());
        fileDB.setType(file.getContentType());
        return fileMetadataRepository.save(fileDB);
    }

    public void deleteFile(String fileId) {
        // TODO: Implement exception handling
        fileMetadataRepository.deleteById(fileId);
    }
}
