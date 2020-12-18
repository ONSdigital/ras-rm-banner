package uk.gov.onsdigital.banner;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.Iterator;
import java.util.List;

import com.google.cloud.datastore.Batch.Response;

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
import org.springframework.web.bind.annotation.PutMapping;
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
      LOGGER.info("Retrieving banner" + id,
        kv("severity", "DEBUG"),
        kv("id", id));
      Long longId = Long.valueOf(id);
      return bannerRepo.findById(longId)
        .map(b -> {
          LOGGER.info("Banner retrieved", 
            kv("banner", b),
            kv("severity", "INFO"));
          return ResponseEntity.ok().body(b);
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("")
  public ResponseEntity<BannerModel> createBanner(
      @RequestBody BannerModel banner) {
    LOGGER.info("saving banner", kv("banner", banner));
    banner.setActive(false);
    BannerModel savedBanner = bannerRepo.save(banner);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedBanner);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BannerModel> removeBanner(
      @PathVariable("id") String id) {
    try {
      Long longId = Long.valueOf(id);
      LOGGER.info("Removing banner", 
        kv("id", id));
      bannerRepo.deleteById(longId);
      return ResponseEntity.noContent().build();
    } catch(NumberFormatException e) {
      LOGGER.info("supplied path variable is not a number");
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("")
  public ResponseEntity<BannerModel> updateBanner(
      @RequestBody BannerModel banner) {
    LOGGER.info("updating banner", kv("banner", banner));
    if (banner == null) {
      LOGGER.warn("Supplied banner cannot be null");
      return ResponseEntity.badRequest().build();
    }

    BannerModel bannerToSave = bannerRepo.findById(banner.getId())
      .map(b -> {
        LOGGER.info("Updating banner", kv("oldBanner", b), 
          kv("newTitle", banner.getTitle()), 
          kv("newContent", banner.getContent()));
        b.setContent(banner.getContent());
        b.setTitle(banner.getTitle());
        return b;
      })
      .orElse(banner);
    if (bannerToSave == banner) {
      LOGGER.info("No changes detected, banner will not be updated", 
        kv("banner", banner));
      return ResponseEntity.noContent().build();
    }
    BannerModel savedBanner = bannerRepo.save(bannerToSave);
    return ResponseEntity.ok(savedBanner);
  }
}
