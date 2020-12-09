package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
