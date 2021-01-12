package uk.gov.onsdigital.banner;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
public class BannerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

  @Autowired
  private BannerService bannerService;
  
  @GetMapping("")
  public ResponseEntity<List<BannerModel>> getBanners() {
    List<BannerModel> banners = bannerService.getAllBanners();
    return ResponseEntity.ok()
      .body(banners);
  }

  @GetMapping("/active")
  public ResponseEntity<BannerModel> getActiveBanner() {
    return bannerService.getActiveBanner()
      .map(ResponseEntity.ok()::body)
      .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BannerModel> getBanner(
      @PathVariable("id") String id) {
    try {
      BannerModel banner = bannerService.getBanner(id);
      return ResponseEntity.ok().body(banner);
    } catch(NumberFormatException e) {
      LOGGER.error("supplied path variable is not a number", kv("banner_id", id));
      return ResponseEntity.badRequest().build();
    } catch(NoSuchElementException e) {
      LOGGER.error("Banner not found", kv("banner_id", id));
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping("/{id}/active")
  public ResponseEntity<BannerModel> setBannerToActive(
      @PathVariable("id") String id) {
    try {
      BannerModel savedBanner = bannerService.setActiveBanner(id);
      return ResponseEntity.status(HttpStatus.OK)
        .body(savedBanner);
    } catch(NumberFormatException ex) {
      LOGGER.info("supplied path variable is not a number", kv("banner_id", id));
      return ResponseEntity.badRequest().build();
    } catch(IllegalArgumentException ex) {
      LOGGER.info("supplied banner does not exist", kv("banner_id", id));
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("")
  public ResponseEntity<BannerModel> createBanner(
      @RequestBody BannerModel banner) {
    BannerModel savedBanner = bannerService.createBanner(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BannerModel> removeBanner(
      @PathVariable("id") String id) {
    try {
      bannerService.removeBanner(id);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("")
  public ResponseEntity<BannerModel> updateBanner(
      @RequestBody BannerModel banner) {
    try {
      BannerModel savedBanner = bannerService.updateBanner(banner);
      return ResponseEntity.ok(savedBanner);
    } catch(IllegalArgumentException ex) {
      LOGGER.error("Invalid banner supplied", ex);
      return ResponseEntity.badRequest().build();
    }
  }
}
