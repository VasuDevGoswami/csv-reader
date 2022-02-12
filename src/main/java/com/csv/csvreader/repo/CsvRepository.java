package com.csv.csvreader.repo;

import com.csv.csvreader.entity.Csv;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CsvRepository extends JpaRepository<Csv, String> {
    Optional<Csv> findByCode(String code);
}