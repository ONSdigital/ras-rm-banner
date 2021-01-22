package uk.gov.onsdigital.banner.controller;

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
import uk.gov.onsdigital.banner.DatastoreEmulator;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.service.TemplateService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TemplateControllerIT {

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
  private TemplateService templateService;
  
  @Test
  public void willReturnBannersObject() {
    Mockito.when(templateService.getAllTemplates())
      .thenReturn(List.of(TemplateModel.builder().build()));
    List<TemplateModel> banners = this.restTemplate.getForObject("http://localhost:" + port + "/banner",
        List.class);
    
    assertEquals(1, banners.size());
  }

  @Test
  public void willReturnASingleBannerObject() {
    TemplateModel expectedBanner = TemplateModel.builder().build();
    Mockito.when(templateService.getBanner("1"))
      .thenReturn(expectedBanner);
    TemplateModel actualBanner = this.restTemplate.getForObject("http://localhost:" + port + "/template/1",
      TemplateModel.class);
    
    assertEquals(expectedBanner, actualBanner);
  }

  @Test
  public void willCreateBanner() {
    TemplateModel postedBanner = TemplateModel.builder()
      .title("BannerTitle")
      .active(false)
      .content("Banner Content")
      .build();

    Mockito.when(templateService.createBanner(ArgumentMatchers.any()))
      .thenReturn(postedBanner);
    ResponseEntity<TemplateModel> resp = this.restTemplate.postForEntity("http://localhost:" + port + "/template",
      postedBanner, TemplateModel.class);
    
    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
    assertEquals(postedBanner, resp.getBody());
  }

  @Test
  public void willRemoveBanner() {
    this.restTemplate.delete(URI.create("http://localhost:" + port + "/template/1"));
    
    Mockito.verify(templateService).removeBanner("1");
  }
}
