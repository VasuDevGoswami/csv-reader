package com.csv.csvreader.controller;

import com.csv.csvreader.entity.Csv;
import com.csv.csvreader.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/csv")
public class CsvController {

    @Autowired
    CSVService csvService;

    @PostMapping
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        if (isFileValid(file)) {
            try {
                csvService.save(file);
                message = "File uploaded successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Failed uploading file : " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Invalid file format!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @GetMapping
    public ResponseEntity<List<Csv>> getAllCsv() {
        List<Csv> csvList = csvService.fetchAllData();
        if (csvList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(csvList, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getCsvByCode(@PathVariable("code") String code) {
        try {
            Csv csvList = csvService.fetchByCode(code);
            return new ResponseEntity<>(csvList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteAllData() {
        String message;
        try {
            csvService.deleteAllData();
            message = "All data removed successfully: ";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Failed removing data!";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
    }

    public static boolean isFileValid(MultipartFile file) {
        return "text/csv".equals(file.getContentType());
    }
}
