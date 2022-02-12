package com.csv.csvreader.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Csv {
  String source;
  String codeListCode;
  @Id
  String code;
  String displayValue;
  String longDescription;
  String fromDate;
  String toDate;
  long sortingPriority;
}