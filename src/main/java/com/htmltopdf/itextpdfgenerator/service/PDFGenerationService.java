package com.htmltopdf.itextpdfgenerator.service;

import com.htmltopdf.itextpdfgenerator.utils.EndPosition;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;

/**
 * @author suresh.thakare
 * 2022-07-22 at 8:24 AM
 */

@Service
public class PDFGenerationService {

  private final Logger log = LoggerFactory.getLogger(PDFGenerationService.class);

  public byte[] generatePDF(final String inputHtml) {
    log.info("Parsing Html Text {} To PDF", inputHtml);
    File tempFile = null;
    PdfWriter writer = null;
    PdfDocument pdf = null;
    Document document = null;
    try {
      tempFile = File.createTempFile("temp_file", ".pdf");
      writer = new PdfWriter(tempFile);
      pdf = new PdfDocument(writer);
      pdf.setDefaultPageSize(new PageSize(595, 14400));
      ConverterProperties properties = new ConverterProperties();
      long start = System.currentTimeMillis();
      document = HtmlConverter
              .convertToDocument(new ByteArrayInputStream(inputHtml.getBytes()), pdf, properties);
      caculateContentLengthAndSetPdfLayout(pdf, document, start);
      return FileUtils.readFileToByteArray(tempFile);
    } catch (IOException ex) {
      log.error("Error Occurred while generating pdf", ex);
      throw new RuntimeException("Error Occurred while generating pdf", ex);
    } finally {
      tempFile.deleteOnExit();
      // Closing pdf PdfDocument
      if (null != pdf) {
        pdf.close();
      }
      // Closing pdf writer
      if (null != writer) {
        try {
          writer.close();
        } catch (IOException e) {
          log.error("Error Occurred while closing PdfWriter", e);
        }
      }
    }
  }

  private void caculateContentLengthAndSetPdfLayout(PdfDocument pdf, Document document, long start) {
    log.info("Time taken to convert html {} ms", System.currentTimeMillis() - start);
    long positionStart = System.currentTimeMillis();
    EndPosition endPosition = new EndPosition();
    LineSeparator separator = new LineSeparator(endPosition);
    document.add(separator);
    document.getRenderer().close();
    PdfPage page = pdf.getPage(1);
    float y = endPosition.getY() - 36;
    page.setMediaBox(new Rectangle(0, y, 595, 14400 - y));
    document.close();
    log.info("Time taken to find Position  {} ms", System.currentTimeMillis() - positionStart);
  }
}
