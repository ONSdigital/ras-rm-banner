package uk.gov.onsdigital.banner.controller;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.service.BannerService;

@RestController
@RequestMapping("/banner")
public class BannerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

  @Autowired
  private BannerService bannerService;
  
  @GetMapping("")
  public ResponseEntity<List<TemplateModel>> getBanner() {
    List<TemplateModel> banners = bannerService.getAllBanners();
    return ResponseEntity.ok()
      .body(banners);
  }

  @PostMapping("")
  public ResponseEntity<TemplateModel> createBanner(
      @RequestBody TemplateModel banner) {
    TemplateModel savedBanner = bannerService.createBanner(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<TemplateModel> removeBanner(
      @PathVariable("id") String id) {
    try {
      bannerService.removeBanner(id);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }
}
