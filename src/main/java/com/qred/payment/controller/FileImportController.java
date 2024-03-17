package com.qred.payment.controller;

import com.qred.payment.service.FileImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/file-import")
public class FileImportController {

  private final FileImportService fileImportService;

  @Autowired
  public FileImportController(FileImportService fileImportService) {
    this.fileImportService = fileImportService;
  }

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> importFile(@RequestPart("file") FilePart file) {
    return fileImportService.processFile(file);
  }

}
