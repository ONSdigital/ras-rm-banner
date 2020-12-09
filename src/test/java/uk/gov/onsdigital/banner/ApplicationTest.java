package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeoutException;

import com.google.api.client.util.Data;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import com.google.datastore.v1.client.DatastoreHelper;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {

  @BeforeAll
  public static void setUpClass() throws Exception {
    DatastoreEmulator.startDatastore();
  }

  @AfterAll
  public static void cleanUpClass() throws Exception {
    DatastoreEmulator.stopDatastore();
  }
  
  @Autowired
  private BannerController controller;

  @Test
  public void loadsContext() {
    assertNotNull(controller);
  }
}