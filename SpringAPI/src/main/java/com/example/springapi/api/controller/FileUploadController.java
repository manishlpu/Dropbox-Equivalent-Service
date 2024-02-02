package com.example.springapi.api.controller;

import com.example.springapi.api.model.FileDB;
import com.example.springapi.api.model.ResponseFile;
import com.example.springapi.api.model.ResponseMessage;
import com.example.springapi.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/files")
@CrossOrigin("http://localhost:8080")
public class FileUploadController {

    @Autowired
    private FileUploadService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        String uniqueId = "";
        try {
            storageService.store(file);
            uniqueId = storageService.getId();
            message = "Uploaded the file successfully: " + file.getOriginalFilename() + "with unique file indetifyer: " + uniqueId;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" + e;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateFile(@RequestParam("file") MultipartFile file, @PathVariable String id) {
        String message = "";
        try {
            storageService.updateFile(file, id);
            message = "Updated the file successfully: " + file.getOriginalFilename() + "with unique file indetifyer: " + id;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not update the file: " + file.getOriginalFilename() + "!" + e;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable (name = "id") String id) {
        FileDB fileDB = storageService.getFile(id);
        ByteArrayResource resource = new ByteArrayResource(fileDB.getData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String id) {
        ResponseMessage responseMessage = new ResponseMessage();
        storageService.deleteFile(id);
        responseMessage.setMessage("File deleted successfully with Id: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}

