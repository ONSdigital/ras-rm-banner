package uk.gov.onsdigital.banner;

import java.lang.invoke.MethodHandles;

import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import com.google.datastore.v1.client.DatastoreHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatastoreEmulator {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
  public static LocalDatastoreHelper emulator;

  public static void startDatastore() {
    logger.info("[Datastore-Emulator] start");
    try {
      emulator = LocalDatastoreHelper.newBuilder().setStoreOnDisk(false).build();
      emulator.start();
    } catch(Exception e) {
      logger.error("Datastore emulator error", e);
    }
    logger.info("[Datastore-Emulator] listening on port {}", emulator.getPort());
    System.setProperty(DatastoreHelper.LOCAL_HOST_ENV_VAR, "localhost:" + emulator.getPort());
  }

  public static void stopDatastore() {
    logger.info("[Datastore-Emulator] stop");
    try {
      emulator.stop();  
    } catch (Exception e) {
      logger.error("Datastore emulator error", e);
    }
  }

  public static void clearDatastore() {
    logger.info("[Datastore-Emulator] cleaning data");
    try {
      emulator.reset();
    } catch (Exception e) {
      logger.error("Datastore emulator error", e);
    }
  }
}
