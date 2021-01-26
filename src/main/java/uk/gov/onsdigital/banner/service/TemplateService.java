package uk.gov.onsdigital.banner.service;

import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.repository.TemplateRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
public class TemplateService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);
  
  @Autowired
  private TemplateRepository bannerRepo;

  public TemplateModel createTemplate(TemplateModel banner) {
    LOGGER.info("saving banner", kv("banner", banner));
    return bannerRepo.save(banner);
  }

  public TemplateModel updateTemplate(TemplateModel banner) {
    LOGGER.info("updating banner", kv("banner", banner));
    if (banner == null) {
      LOGGER.warn("Supplied banner cannot be null");
      throw new IllegalArgumentException("null banner supplied for updating");
    }

    TemplateModel bannerToSave = bannerRepo.findById(banner.getId())
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

  public void removeTemplate(String bannerId) {
    Long longId = Long.valueOf(bannerId);
    LOGGER.info("Removing banner", 
      kv("id", bannerId));
    bannerRepo.deleteById(longId);
    LOGGER.info("banner removed", 
      kv("id", bannerId));
  }

  public List<TemplateModel> getAllTemplates() {
    LOGGER.info("Retrieving all banners");
    Iterator<TemplateModel> bannerIter = bannerRepo.findAll().iterator();
    return IteratorUtils.toList(bannerIter);
  }

  public TemplateModel getTemplate(String id) {
    LOGGER.info("Retrieving banner",
        kv("severity", "DEBUG"),
        kv("id", id));
    Long longId = Long.valueOf(id);
    Optional<TemplateModel> banner = bannerRepo.findById(longId);
    if (banner.isPresent()) {
      LOGGER.info("Banner retrieved", 
          kv("banner", banner),
          kv("severity", "INFO"));
    }
    return banner.orElseThrow();
  }
}
