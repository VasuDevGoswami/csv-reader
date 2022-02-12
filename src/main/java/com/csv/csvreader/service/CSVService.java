package com.csv.csvreader.service;

import com.csv.csvreader.entity.Csv;
import com.csv.csvreader.repo.CsvRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CSVService {
  @Autowired
  CsvRepository csvRepository;

  public void save(MultipartFile file) {
    try {
      List<Csv> csvList = csvToCsv(file.getInputStream());
      csvRepository.saveAll(csvList);
    } catch (IOException e) {
      throw new RuntimeException("failed to save csv data: " + e.getMessage());
    }
  }
  public List<Csv> fetchAllData() {
    return csvRepository.findAll();
  }

  public void deleteAllData() {
    csvRepository.deleteAll();
    log.info("All data deleted");
  }

  public Csv fetchByCode(String code) {
    Optional<Csv> csv = csvRepository.findByCode(code);
    if (csv.isPresent()) {
      return csv.get();
    }
    throw new NoSuchElementException("Code not found : "+code);
  }

  public static List<Csv> csvToCsv(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
         CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
      List<Csv> csvList = new ArrayList<Csv>();
      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        Csv csv = new Csv(
                csvRecord.get("source"),
                csvRecord.get("codeListCode"),
                csvRecord.get("code"),
                csvRecord.get("displayValue"),
                csvRecord.get("longDescription"),
                csvRecord.get("fromDate"),
                csvRecord.get("toDate"),
                csvRecord.get("sortingPriority").isEmpty() ? 0:
                        Long.parseLong(csvRecord.get("sortingPriority"))
               );
        csvList.add(csv);
      }
      return csvList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }
}