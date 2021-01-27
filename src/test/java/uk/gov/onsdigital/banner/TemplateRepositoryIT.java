package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.repository.TemplateRepository;

@SpringBootTest
public class TemplateRepositoryIT {
  
  @Autowired
  private TemplateRepository bannerRepo;

  @BeforeAll
  public static void setUpClass() {
    DatastoreEmulator.startDatastore();
  }

  @AfterAll
  public static void cleanUpClass() {
    DatastoreEmulator.stopDatastore();
  }

  @BeforeEach
  public void init() {
    DatastoreEmulator.clearDatastore();
  }

  @Test
  public void willSaveToDatastore() {
    TemplateModel model =
      bannerRepo.save(TemplateModel.builder()
        .title("A Banner")
        .content("Hello world")
        .build());
    
    assertEquals("A Banner", model.getTitle());
    assertEquals("Hello world", model.getContent());
    assertNotNull(model.getId());
  }

  @Test
  public void willGetFromDatastore() {
    TemplateModel model =
      bannerRepo.save(TemplateModel.builder()
        .title("A Banner")
        .content("Hello world")
        .build());
    
    assertEquals("A Banner", model.getTitle());
    assertEquals("Hello world", model.getContent());

    TemplateModel retrievedModel = bannerRepo.findById(model.getId()).get();

    assertEquals(model, retrievedModel);
  }

  @Test
  public void willDeleteFromDatastore() {
    TemplateModel model =
      bannerRepo.save(TemplateModel.builder()
        .title("A Banner")
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    bannerRepo.deleteById(model.getId());

    assertFalse(bannerRepo.findById(model.getId()).isPresent());
  }

  @Test
  public void willUpdateInDatastore() {
    TemplateModel model =
      bannerRepo.save(TemplateModel.builder()
        .title("A Banner")
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    model.setTitle("Another banner");
    bannerRepo.save(model);

    assertEquals(bannerRepo.findById(model.getId()).get().getTitle(), "Another banner");
  }
}
