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
    List<TemplateModel> banners = templateService.getAllTemplates();
    return ResponseEntity.ok()
      .body(banners);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TemplateModel> getTemplate(
      @PathVariable("id") String id) {
    try {
      TemplateModel banner = templateService.getBanner(id);
      return ResponseEntity.ok().body(banner);
    } catch(NumberFormatException e) {
      LOGGER.error("supplied path variable is not a number", kv("banner_id", id));
      return ResponseEntity.badRequest().build();
    } catch(NoSuchElementException e) {
      LOGGER.error("Banner not found", kv("banner_id", id));
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("")
  public ResponseEntity<TemplateModel> createTemplate(
      @RequestBody TemplateModel banner) {
    TemplateModel savedBanner = templateService.createBanner(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<TemplateModel> removeTemplate(
      @PathVariable("id") String id) {
    try {
      templateService.removeBanner(id);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("")
  public ResponseEntity<TemplateModel> updateTemplate(
      @RequestBody TemplateModel banner) {
    try {
      TemplateModel savedBanner = templateService.updateBanner(banner);
      return ResponseEntity.ok(savedBanner);
    } catch(IllegalArgumentException ex) {
      LOGGER.error("Invalid banner supplied", ex);
      return ResponseEntity.badRequest().build();
    }
  }
}
