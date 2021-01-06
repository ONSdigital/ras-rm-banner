package uk.gov.onsdigital.banner;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerService.class);
  
  @Autowired
  private BannerRepository bannerRepo;

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
}
