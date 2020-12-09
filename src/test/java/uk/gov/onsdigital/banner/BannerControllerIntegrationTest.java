package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BannerControllerIntegrationTest {

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
}
