package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
  private BannerRepository bannerRepo;
  
  @Test
  public void willReturnBannersObject() {
    Mockito.when(bannerRepo.findAll())
      .thenReturn(List.of(BannerModel.builder().build()));
    List<BannerModel> banners = this.restTemplate.getForObject("http://localhost:" + port + "/banner",
        List.class);
    
    assertEquals(1, banners.size());
  }

  @Test
  public void willReturnASingleBannerObject() {
    BannerModel expectedBanner = BannerModel.builder().build();
    Mockito.when(bannerRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.of(expectedBanner));
    BannerModel actualBanner = this.restTemplate.getForObject("http://localhost:" + port + "/banner/1",
      BannerModel.class);
    
    assertEquals(expectedBanner, actualBanner);
  }

  @Test
  public void willCreateBanner() {
    BannerModel postedBanner = BannerModel.builder()
      .title("BannerTitle")
      .active(false)
      .content("Banner Content")
      .build();

    Mockito.when(bannerRepo.save(ArgumentMatchers.any()))
      .thenReturn(postedBanner);
    ResponseEntity<BannerModel> resp = this.restTemplate.postForEntity("http://localhost:" + port + "/banner", 
      postedBanner, BannerModel.class);
    
    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    assertEquals(postedBanner, resp.getBody());
  }
}
