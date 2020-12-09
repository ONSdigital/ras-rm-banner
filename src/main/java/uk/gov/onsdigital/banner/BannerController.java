package uk.gov.onsdigital.banner;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
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

@RestController
@RequestMapping("/banner")
public class BannerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerController.class);

  @Autowired
  private BannerRepository bannerRepo;
  
  @GetMapping("")
  public ResponseEntity<List<BannerModel>> getBanners() {
    Iterator<BannerModel> bannerIter = bannerRepo.findAll().iterator();
    return ResponseEntity.ok()
      .body(IteratorUtils.toList(bannerIter));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BannerModel> getBanner(
      @PathVariable("id") String id) {
    try {
      LOGGER.info("Retrieving banner for ID: " + id);
      Long longId = Long.valueOf(id);
      return bannerRepo.findById(longId)
        .map(t -> ResponseEntity.ok().body(t))
        .orElseGet(() -> ResponseEntity.notFound().build());
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("")
  public ResponseEntity<BannerModel> createBanner(
      @RequestBody BannerModel banner) {
    BannerModel savedBanner = bannerRepo.save(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BannerModel> removeBanner(
      @PathVariable("id") String id) {
    try {
      Long longId = Long.valueOf(id);
      LOGGER.info("Removing banner for ID: " + id);
      bannerRepo.deleteById(longId);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }
}
