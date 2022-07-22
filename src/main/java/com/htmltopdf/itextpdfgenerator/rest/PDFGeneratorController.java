package com.htmltopdf.itextpdfgenerator.rest;

import com.htmltopdf.itextpdfgenerator.service.PDFGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author suresh.thakare
 * 2022-07-22 at 8:26 AM
 */

@RestController

public class PDFGeneratorController {

  private final Logger LOGGER = LoggerFactory.getLogger(PDFGeneratorController.class);

  final PDFGenerationService pdfGenerationService;

  public PDFGeneratorController(PDFGenerationService pdfGenerationService) {
    this.pdfGenerationService = pdfGenerationService;
  }

  @RequestMapping(value = "generate/pdf", produces = {"application/pdf"}, consumes = {"application/json"}, method = RequestMethod.GET)

  public ResponseEntity<Resource> getInterpollatedResult() {
    LOGGER.info("REST REQUEST to generate PDF");
    try {
      File file = ResourceUtils.getFile("classpath:html/itinerarySample.html");
      if (file.exists()) {
        byte[] fileData = Files.readAllBytes(file.toPath());
        String fileContent = new String(fileData);
        LOGGER.info("data.txt file content:");
        LOGGER.info(fileContent);
        byte[] pdfByte = pdfGenerationService.generatePDF(fileContent);
        ByteArrayResource resource = new ByteArrayResource(pdfByte);
        LOGGER.info("PDF generation completed");
        return ResponseEntity.ok().contentLength(pdfByte.length).contentType(MediaType.parseMediaType("application/pdf")).body(resource);
      }

    } catch (Exception e) {
      LOGGER.error("[*** ERROR ***] Error Occurred while generating PDF", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf")).body(null);
  }
}
