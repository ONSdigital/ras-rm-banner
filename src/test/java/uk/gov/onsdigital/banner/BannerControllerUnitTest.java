package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;

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

@ExtendWith(MockitoExtension.class)
public class BannerControllerUnitTest {
  
  @InjectMocks
  private BannerController bannerController;

  @Mock
  private BannerRepository bannerRepo;

  @Mock
  private BannerService bannerService;

  @Test
  public void willReturn200() {
    ResponseEntity<List<BannerModel>> resp = bannerController.getBanners();

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturnEmptyListIfNoData() {
    ResponseEntity<List<BannerModel>> resp = bannerController.getBanners();

    assertEquals(0, resp.getBody().size());
  }

  @Test
  public void willReturnBannersIfData() {
    ResponseEntity<List<BannerModel>> resp = bannerController.getBanners();
    
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturnSingleBanner() {
    ResponseEntity<BannerModel> resp = bannerController.getBanner("1");

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturn404IfNoBannerFound() {
    Mockito.when(bannerService.getBanner("1"))
      .thenThrow(new NoSuchElementException());
    ResponseEntity<BannerModel> resp = bannerController.getBanner("1");

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumber() {
    Mockito.when(bannerService.getBanner("abc"))
      .thenThrow(new NumberFormatException());
    ResponseEntity<BannerModel> resp = bannerController.getBanner("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willCreateBanner() {
    ResponseEntity<BannerModel> resp = bannerController.createBanner(new BannerModel());

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  public void willRemoveBanner() {
    ResponseEntity<BannerModel> resp = bannerController.removeBanner("1");

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumberOnDelete() {
    Mockito.doThrow(new NumberFormatException())
      .when(bannerService)
      .removeBanner("abc");
    ResponseEntity<BannerModel> resp = bannerController.removeBanner("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willUpdateBanner() {
    BannerModel newBanner = BannerModel.builder().title("1").id(1L).build();
    Mockito.when(bannerService.updateBanner(newBanner))
      .thenReturn(newBanner);

    ResponseEntity<BannerModel> resp = bannerController.updateBanner(newBanner);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturn400IfNullBannerSuppliedForUpdate() {
    Mockito.when(bannerService.updateBanner(null))
      .thenThrow(new IllegalArgumentException());
    ResponseEntity<BannerModel> resp = bannerController.updateBanner(null);

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willReturn204NoActiveBanner() {
    ResponseEntity<BannerModel> resp = bannerController.getActiveBanner();

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());

    Mockito.verify(bannerRepo).findActiveBanner();
  }

  @Test
  public void willReturn200IfActiveBanner() {
    BannerModel banner = BannerModel.builder().title("1").active(true).build();
    Mockito.when(bannerRepo.findActiveBanner())
      .thenReturn(Optional.of(banner));
    ResponseEntity<BannerModel> resp = bannerController.getActiveBanner();

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals(banner, resp.getBody());

    Mockito.verify(bannerRepo).findActiveBanner();
  }
}
