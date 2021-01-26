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
import uk.gov.onsdigital.banner.model.BannerModel;
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
    ResponseEntity<BannerModel> resp = bannerController.getBanner();
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturn204IfNoData() {
    ResponseEntity<BannerModel> resp = bannerController.getBanner();
    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  public void willCreateBanner() {
    ResponseEntity<BannerModel> resp = bannerController.createBanner(new BannerModel());
    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  public void willRemoveBanner() {
    ResponseEntity<BannerModel> resp = bannerController.removeBanner();
    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }
}
