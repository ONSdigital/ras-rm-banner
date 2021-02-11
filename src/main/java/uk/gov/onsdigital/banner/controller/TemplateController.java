package uk.gov.onsdigital.banner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.service.TemplateService;

import java.util.List;
import java.util.NoSuchElementException;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RestController
@RequestMapping("/template")
public class TemplateController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

  @Autowired
  private TemplateService templateService;
  
  @GetMapping("")
  public ResponseEntity<List<TemplateModel>> getTemplates() {
    List<TemplateModel> templates = templateService.getAllTemplates();
    return ResponseEntity.ok()
      .body(templates);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TemplateModel> getTemplate(
      @PathVariable("id") String id) {
    try {
      TemplateModel banner = templateService.getTemplate(id);
      return ResponseEntity.ok().body(banner);
    } catch(NumberFormatException e) {
      LOGGER.error("supplied path variable is not a number", kv("banner_id", id));
      return ResponseEntity.badRequest().build();
    } catch(NoSuchElementException e) {
      LOGGER.error("Template not found", kv("banner_id", id));
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("")
  public ResponseEntity<TemplateModel> createTemplate(
      @RequestBody TemplateModel template) {
    TemplateModel savedTemplate = templateService.createTemplate(template);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedTemplate);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<TemplateModel> removeTemplate(
      @PathVariable("id") String id) {
    try {
      templateService.removeTemplate(id);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("")
  public ResponseEntity<TemplateModel> updateTemplate(
      @RequestBody TemplateModel template) {
    try {
      TemplateModel savedTemplate = templateService.updateTemplate(template);
      return ResponseEntity.ok(savedTemplate);
    } catch(IllegalArgumentException ex) {
      LOGGER.error("Invalid template supplied", ex);
      return ResponseEntity.badRequest().build();
    }
  }
}
