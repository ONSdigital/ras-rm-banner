package uk.gov.onsdigital.banner.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.onsdigital.banner.controller.BannerController;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.service.BannerService;

@ExtendWith(MockitoExtension.class)
public class BannerControllerUnitTest {
  
  @InjectMocks
  private BannerController bannerController;

  @Mock
  private BannerService bannerService;

  @Test
  public void willReturn200() {
    ResponseEntity<List<TemplateModel>> resp = bannerController.getBanner();

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturnEmptyListIfNoData() {
    ResponseEntity<List<TemplateModel>> resp = bannerController.getBanner();

    assertEquals(0, resp.getBody().size());
  }

  @Test
  public void willReturnBannersIfData() {
    ResponseEntity<List<TemplateModel>> resp = bannerController.getBanner();
    
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willCreateBanner() {
    ResponseEntity<TemplateModel> resp = bannerController.createBanner(new TemplateModel());

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  public void willRemoveBanner() {
    ResponseEntity<TemplateModel> resp = bannerController.removeBanner("1");

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumberOnDelete() {
    Mockito.doThrow(new NumberFormatException())
      .when(bannerService)
      .removeBanner("abc");
    ResponseEntity<TemplateModel> resp = bannerController.removeBanner("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }
}
