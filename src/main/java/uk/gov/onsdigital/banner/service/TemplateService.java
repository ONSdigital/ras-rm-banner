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
import java.util.NoSuchElementException;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
public class TemplateService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TemplateService.class);
  
  @Autowired
  private TemplateRepository templateRepo;

  public TemplateModel createTemplate(TemplateModel template) {
    LOGGER.info("saving template", kv("template", template));
    return templateRepo.save(template);
  }

  public TemplateModel updateTemplate(TemplateModel template) {
    LOGGER.info("updating template", kv("template", template));
    if (template == null) {
      LOGGER.warn("Supplied template cannot be null");
      throw new IllegalArgumentException("null template supplied for updating");
    }

    try {
      TemplateModel templateToSave = templateRepo.findById(template.getId())
        .map(b -> {
          LOGGER.info("Updating template", kv("oldtemplate", b),
                  kv("newTitle", template.getTitle()),
                  kv("newContent", template.getContent()));
          b.setContent(template.getContent());
          b.setTitle(template.getTitle());
          return b;
        }).orElseThrow();
      if (templateToSave.equals(template)) {
        LOGGER.info("No changes detected, template will not be updated",
                kv("template", template));
        return template;
      }
      LOGGER.info("Saving updated template to database", kv("template", template));
      return templateRepo.save(templateToSave);
    } catch (NoSuchElementException e) {
      LOGGER.info("Template doesn't exist, creating new one", kv("template", template));
      return createTemplate(template);
    }
  }

  public void removeTemplate(String templateId) {
    Long longId = Long.valueOf(templateId);
    LOGGER.info("Removing template",
      kv("id", templateId));
    templateRepo.deleteById(longId);
    LOGGER.info("template removed",
      kv("id", templateId));
  }

  public List<TemplateModel> getAllTemplates() {
    LOGGER.info("Retrieving all templates");
    Iterator<TemplateModel> templateIter = templateRepo.findAll().iterator();
    return IteratorUtils.toList(templateIter);
  }

  public TemplateModel getTemplate(String id) {
    LOGGER.info("Retrieving template",
        kv("severity", "DEBUG"),
        kv("id", id));
    Long longId = Long.valueOf(id);
    Optional<TemplateModel> template = templateRepo.findById(longId);
    if (template.isPresent()) {
      LOGGER.info("Template retrieved",
          kv("template", template),
          kv("severity", "INFO"));
    }
    return template.orElseThrow();
  }
}
