package uk.gov.onsdigital.banner.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.onsdigital.banner.model.BannerModel;
import uk.gov.onsdigital.banner.repository.BannerRepository;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BannerServiceUnitTest {

  @InjectMocks
  private BannerService bannerService;

  @Mock
  private BannerRepository bannerRepo;

  @Test
  public void willReturnSingleBanner() {
    BannerModel expected1 = BannerModel.builder().content("content text").build();
    Mockito.when(bannerRepo.findById("active"))
        .thenReturn(Optional.of(expected1));

    BannerModel banner = bannerService.getBanner("active");

    assertEquals(expected1, banner);
  }

  @Test
  public void willThrowIfNoBannerPresent() {
    Mockito.when(bannerRepo.findById("active"))
        .thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> bannerService.getBanner("active"));
  }
}
