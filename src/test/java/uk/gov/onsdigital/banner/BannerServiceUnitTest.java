package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BannerServiceUnitTest {
  
  @InjectMocks
  private BannerService bannerService;

  @Mock
  private BannerRepository bannerRepo;

  @Test
  public void willSetABannerToActive() {
    BannerModel banner = BannerModel.builder().id(1L).active(false).build();
    Mockito.spy(banner);

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    
    bannerService.setActiveBanner("1");

    assertTrue(banner.getActive());
    Mockito.verify(bannerRepo).save(banner);
  }

  @Test
  public void willReturnBannerWithoutSavingIfAlreadyActive() {
    BannerModel banner = BannerModel.builder().id(1L).active(true).build();

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    
    bannerService.setActiveBanner("1");

    Mockito.verify(bannerRepo, never()).save(banner);
  }

  @Test
  public void willMakeCurrentActiveBannerInactive() {
    BannerModel banner = BannerModel.builder().id(1L).active(false).build();
    BannerModel currentActiveBanner = BannerModel.builder().id(2L).active(true).build();

    Mockito.spy(currentActiveBanner);

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    Mockito.when(bannerRepo.findActiveBanner())
      .thenReturn(Optional.of(currentActiveBanner));
    
    bannerService.setActiveBanner("1");

    assertFalse(currentActiveBanner.getActive());
    Mockito.verify(bannerRepo, times(2)).save(any(BannerModel.class));
  }

  @Test
  public void willWillDefaultToFalseOnBannerCreate() {
    BannerModel spyModel = Mockito.spy(BannerModel.class);
    assertNull(spyModel.getActive());
    bannerService.createBanner(spyModel);

    assertFalse(spyModel.getActive());
    Mockito.verify(bannerRepo).save(spyModel);
  }
  
}
