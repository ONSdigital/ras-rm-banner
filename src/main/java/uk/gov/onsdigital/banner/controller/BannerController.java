package uk.gov.onsdigital.banner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.onsdigital.banner.model.BannerModel;
import uk.gov.onsdigital.banner.service.BannerService;

@RestController
@RequestMapping("/banner")
public class BannerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

  @Autowired
  private BannerService bannerService;
  
  @GetMapping("")
  public ResponseEntity<BannerModel> getBanner() {
    BannerModel banner = bannerService.getBanner("active");
    return ResponseEntity.ok()
      .body(banner);
  }

  @PostMapping("")
  public ResponseEntity<BannerModel> createBanner(
      @RequestBody BannerModel banner) {
    BannerModel savedBanner = bannerService.createBanner(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("")
  public ResponseEntity<BannerModel> removeBanner() {
      bannerService.removeBanner("active");
      return ResponseEntity.noContent().build();
  }
}
