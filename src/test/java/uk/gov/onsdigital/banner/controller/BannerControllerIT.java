package uk.gov.onsdigital.banner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.net.URI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.onsdigital.banner.DatastoreEmulator;
import uk.gov.onsdigital.banner.model.BannerModel;
import uk.gov.onsdigital.banner.service.BannerService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BannerControllerIT {

  @BeforeAll
  public static void setUpClass() {
    DatastoreEmulator.startDatastore();
  }

  @AfterAll
  public static void cleanUpClass() {
    DatastoreEmulator.stopDatastore();
  }

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private BannerService bannerService;

  @Test
  public void willReturnASingleBannerObject() {
    BannerModel expectedBanner = BannerModel.builder().build();
    Mockito.when(bannerService.getBanner("active"))
        .thenReturn(expectedBanner);
    BannerModel actualBanner = this.restTemplate.getForObject("http://localhost:" + port + "/banner",
        BannerModel.class);

    assertEquals(expectedBanner, actualBanner);
  }

  @Test
  public void willCreateBanner() {
    BannerModel postedBanner = BannerModel.builder()
        .content("Banner Content")
        .build();

    Mockito.when(bannerService.createBanner(ArgumentMatchers.any()))
        .thenReturn(postedBanner);
    ResponseEntity<BannerModel> resp = this.restTemplate.postForEntity("http://localhost:" + port + "/banner",
        postedBanner, BannerModel.class);

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    assertEquals(postedBanner, resp.getBody());
  }

  @Test
  public void willRemoveBanner() {
    this.restTemplate.delete(URI.create("http://localhost:" + port + "/banner"));

    Mockito.verify(bannerService).removeBanner("active");
  }
}
