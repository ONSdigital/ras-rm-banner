package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

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
        .title("A Banner")
        .active(true)
        .content("Hello world")
        .build());
    
    assertEquals("A Banner", model.getTitle());
    assertTrue(model.getActive());
    assertEquals("Hello world", model.getContent());
    assertNotNull(model.getId());
  }

  @Test
  public void willGetFromDatastore() {
    BannerModel model = 
      bannerRepo.save(BannerModel.builder()
        .title("A Banner")
        .active(true)
        .content("Hello world")
        .build());
    
    assertEquals("A Banner", model.getTitle());
    assertTrue(model.getActive());
    assertEquals("Hello world", model.getContent());

    BannerModel retrievedModel = bannerRepo.findById(model.getId()).get();

    assertEquals(model, retrievedModel);
  }

  @Test
  public void willDeleteFromDatastore() {
    BannerModel model = 
      bannerRepo.save(BannerModel.builder()
        .title("A Banner")
        .active(true)
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
        .title("A Banner")
        .active(true)
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    model.setActive(false);
    bannerRepo.save(model);

    assertFalse(bannerRepo.findById(model.getId()).get().getActive());
  }

  @Test
  public void willReturnActiveBanner() {
    bannerRepo.save(BannerModel.builder()
      .title("A Banner")
      .active(false)
      .content("Hello world")
      .build());
    
    BannerModel model = 
      bannerRepo.save(BannerModel.builder()
        .title("A Banner")
        .active(true)
        .content("Hello world")
        .build());
    
    assertNotNull(model.getId());

    bannerRepo.findById(model.getId());

    Optional<BannerModel> actualModel = bannerRepo.findActiveBanner();

    assertTrue(actualModel.isPresent());
    assertEquals(model.getId(), actualModel.get().getId());
  }

  @Test
  public void willReturnEmptyOptionalIfNoActiveBanner() {
    BannerModel model = bannerRepo.save(BannerModel.builder()
      .title("A Banner")
      .active(false)
      .content("Hello world")
      .build());
    
    assertNotNull(model.getId());

    bannerRepo.findById(model.getId());

    Optional<BannerModel> actualModel = bannerRepo.findActiveBanner();

    assertFalse(actualModel.isPresent());
  }
}
