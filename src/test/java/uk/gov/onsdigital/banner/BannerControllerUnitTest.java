package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;

import java.util.List;
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
    BannerModel expected1 = BannerModel.builder().title("1").build();
    BannerModel expected2 = BannerModel.builder().title("2").build();

    Mockito.when(bannerRepo.findAll())
      .thenReturn(List.of(expected1, expected2));
    ResponseEntity<List<BannerModel>> resp = bannerController.getBanners();
    
    List<BannerModel> actualBanners = resp.getBody();

    assertEquals(2, actualBanners.size());
    assertEquals(expected1, actualBanners.get(0));
    assertEquals(expected2, actualBanners.get(1));
  }

  @Test
  public void willReturnSingleBanner() {
    BannerModel expected1 = BannerModel.builder().title("1").build();
    Mockito.when(bannerRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.of(expected1));

    ResponseEntity<BannerModel> resp = bannerController.getBanner("1");

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertEquals(expected1, resp.getBody());
  }

  @Test
  public void willReturn404IfNoBannerFound() {
    ResponseEntity<BannerModel> resp = bannerController.getBanner("1");

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumber() {
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
    ResponseEntity<BannerModel> resp = bannerController.removeBanner("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willUpdateBanner() {
    BannerModel newBanner = BannerModel.builder().title("1").id(1L).build();
    BannerModel preExisting = BannerModel.builder().title("title").active(false).id(1L).build();
    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(preExisting));

    ResponseEntity<BannerModel> resp = bannerController.updateBanner(newBanner);

    assertEquals(HttpStatus.OK, resp.getStatusCode());

    Mockito.verify(bannerRepo, never()).save(newBanner);
    Mockito.verify(bannerRepo).save(preExisting);
    assertFalse(preExisting.getActive());
    assertEquals("1", preExisting.getTitle());
  }

  @Test
  public void willReturn204IfNoChangesToYpdate() {
    BannerModel expected = BannerModel.builder().title("1").id(1L).build();
    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(expected));
    ResponseEntity<BannerModel> resp = bannerController.updateBanner(expected);

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  public void willReturn400IfNullBannerSuppliedForUpdate() {
    ResponseEntity<BannerModel> resp = bannerController.updateBanner(null);

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willReturn204IfNullBannerIdSuppliedForUpdate() {
    BannerModel banner = BannerModel.builder().title("1").build();
    ResponseEntity<BannerModel> resp = bannerController.updateBanner(banner);

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());

    Mockito.verify(bannerRepo, never()).save(Mockito.any());
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
