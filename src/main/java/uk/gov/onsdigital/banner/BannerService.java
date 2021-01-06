package uk.gov.onsdigital.banner;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerService.class);
  
  @Autowired
  private BannerRepository bannerRepo;

  public Optional<BannerModel> getActiveBanner() {
    LOGGER.info("retrieving currently active banner");
    return bannerRepo.findActiveBanner();
  }

  public BannerModel setActiveBanner(String bannerId) {
    BannerModel foundBanner = 
      bannerRepo.findById(Long.valueOf(bannerId))
        .orElseThrow(() -> new IllegalArgumentException("Banner has not been found in datastore"));
    
    if (foundBanner.getActive()) {
      LOGGER.info("Banner already active", kv("banner", foundBanner));
      return foundBanner;
    }

    LOGGER.info("Banner found in datastore", kv("banner", foundBanner));
    setCurrentActiveBannerToInactive();

    foundBanner.setActive(true);
    bannerRepo.save(foundBanner);
    LOGGER.info("Banner has been set to active", kv("banner", foundBanner));
    return foundBanner;
  }

  /**
   * Search the database for an active banner and set it to inactive.
   * Only 1 banner can be active at any given time
   */
  private void setCurrentActiveBannerToInactive() {
    LOGGER.info("Searching for a currently active banner");
    Optional<BannerModel> banner = bannerRepo.findActiveBanner()
      .map(foundBanner -> {
        LOGGER.info("An active banner has been found", kv("banner", foundBanner));
        foundBanner.setActive(false);
        LOGGER.info("Setting banner to an inactive state", kv("banner", foundBanner));
        return foundBanner;
      })
      .map(bannerRepo::save);
    
    if(!banner.isPresent()) {
      LOGGER.info("No currently active banner has been found");
    }
  }

  public BannerModel createBanner(BannerModel banner) {
    LOGGER.info("saving banner", kv("banner", banner));
    banner.setActive(false);
    return bannerRepo.save(banner);
  }

  public BannerModel updateBanner(BannerModel banner) {
    LOGGER.info("updating banner", kv("banner", banner));
    if (banner == null) {
      LOGGER.warn("Supplied banner cannot be null");
      throw new IllegalArgumentException("null banner supplied for updating");
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
      return banner;
    }
    LOGGER.info("Saving updated banner to database", kv("banner", banner));
    return bannerRepo.save(bannerToSave);
  }

  public void removeBanner(String bannerId) {
    Long longId = Long.valueOf(bannerId);
    LOGGER.info("Removing banner", 
      kv("id", bannerId));
    bannerRepo.deleteById(longId);
    LOGGER.info("banner removed", 
      kv("id", bannerId));
  }

  public List<BannerModel> getAllBanners() {
    LOGGER.info("Retrieving all banners");
    Iterator<BannerModel> bannerIter = bannerRepo.findAll().iterator();
    return IteratorUtils.toList(bannerIter);
  }

  public BannerModel getBanner(String id) {
    LOGGER.info("Retrieving banner" + id,
        kv("severity", "DEBUG"),
        kv("id", id));
    Long longId = Long.valueOf(id);
    Optional<BannerModel> banner = bannerRepo.findById(longId);
    if (banner.isPresent()) {
      LOGGER.info("Banner retrieved", 
          kv("banner", banner),
          kv("severity", "INFO"));
    }
    return banner.orElseThrow();
  }
}
