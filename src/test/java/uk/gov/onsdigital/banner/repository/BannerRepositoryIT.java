package uk.gov.onsdigital.banner.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.onsdigital.banner.DatastoreEmulator;
import uk.gov.onsdigital.banner.model.BannerModel;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BannerRepositoryIT {
  
  @Autowired
  private BannerRepository bannerRepo;

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
    BannerModel model =
      bannerRepo.save(BannerModel.builder()
        .id("active")
        .content("Hello world")
        .build());

    assertEquals("Hello world", model.getContent());
    assertEquals("active", model.getId());
  }

  @Test
  public void willGetFromDatastore() {
    BannerModel model =
      bannerRepo.save(BannerModel.builder()
        .id("active")
        .content("Hello world")
        .build());
    
    assertEquals("active", model.getId());
    assertEquals("Hello world", model.getContent());

    BannerModel retrievedModel = bannerRepo.findById(model.getId()).get();

    assertEquals(model, retrievedModel);
  }

  @Test
  public void willDeleteFromDatastore() {
    BannerModel model =
      bannerRepo.save(BannerModel.builder()
        .id("active")
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    bannerRepo.deleteById(model.getId());

    assertFalse(bannerRepo.findById(model.getId()).isPresent());
  }

  @Test
  public void willUpdateInDatastore() {
    BannerModel model =
      bannerRepo.save(BannerModel.builder()
        .id("active")
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    model.setContent("Another banner");
    bannerRepo.save(model);

    assertEquals(bannerRepo.findById(model.getId()).get().getContent(), "Another banner");
  }
}
