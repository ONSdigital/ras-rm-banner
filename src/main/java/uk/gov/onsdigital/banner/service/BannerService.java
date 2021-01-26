package uk.gov.onsdigital.banner.service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.onsdigital.banner.model.BannerModel;
import uk.gov.onsdigital.banner.repository.BannerRepository;

@Service
public class BannerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BannerService.class);
  
  @Autowired
  private BannerRepository bannerRepo;

  public BannerModel getBanner(String id) {
    LOGGER.info("Retrieving banner",
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

  public BannerModel createBanner(BannerModel banner) {
    banner.setId(1L);
    LOGGER.info("saving banner", kv("banner", banner));
    return bannerRepo.save(banner);
  }

  public void removeBanner(String bannerId) {
    Long longId = Long.valueOf(bannerId);
    LOGGER.info("Removing banner", 
      kv("id", bannerId));
    bannerRepo.deleteById(longId);
    LOGGER.info("banner removed", 
      kv("id", bannerId));
  }
}
