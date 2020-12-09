package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
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
    BannerModel expected1 = BannerModel.builder().title("test1").build();
    BannerModel expected2 = BannerModel.builder().title("test2").build();

    Mockito.when(bannerRepo.findAll())
      .thenReturn(List.of(expected1, expected2));
    ResponseEntity<List<BannerModel>> resp = bannerController.getBanners();
    
    List<BannerModel> actualBanners = resp.getBody();

    assertEquals(2, actualBanners.size());
    assertEquals(expected1, actualBanners.get(0));
    assertEquals(expected2, actualBanners.get(1));
  }
}
