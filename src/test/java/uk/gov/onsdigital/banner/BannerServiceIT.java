package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import uk.gov.onsdigital.banner.service.BannerService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BannerServiceIT {
  
  @Autowired
  private BannerService bannerService;

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
  public void willReturnEmptyListIfNoBannersExist() {
    assertEquals(0, bannerService.getAllBanners().size());
  }
}
