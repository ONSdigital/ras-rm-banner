package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationIT {

  @BeforeAll
  public static void setUpClass() {
    DatastoreEmulator.startDatastore();
  }

  @AfterAll
  public static void cleanUpClass() {
    DatastoreEmulator.stopDatastore();
  }
  
  @Autowired
  private BannerController controller;

  @Test
  public void loadsContext() {
    assertNotNull(controller);
  }
}