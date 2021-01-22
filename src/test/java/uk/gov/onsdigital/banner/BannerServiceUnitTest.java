package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.repository.TemplateRepository;
import uk.gov.onsdigital.banner.service.BannerService;

@ExtendWith(MockitoExtension.class)
public class BannerServiceUnitTest {
  
  @InjectMocks
  private BannerService bannerService;

  @Mock
  private TemplateRepository bannerRepo;

  @Test
  public void willSetABannerToActive() {
    TemplateModel banner = TemplateModel.builder().id(1L).active(false).build();
    Mockito.spy(banner);

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    
    bannerService.setActiveBanner("1");

    assertTrue(banner.getActive());
    Mockito.verify(bannerRepo).save(banner);
  }

  @Test
  public void willReturnBannerWithoutSavingIfAlreadyActive() {
    TemplateModel banner = TemplateModel.builder().id(1L).active(true).build();

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    
    bannerService.setActiveBanner("1");

    Mockito.verify(bannerRepo, never()).save(banner);
  }

  @Test
  public void willMakeCurrentActiveBannerInactive() {
    TemplateModel banner = TemplateModel.builder().id(1L).active(false).build();
    TemplateModel currentActiveBanner = TemplateModel.builder().id(2L).active(true).build();

    Mockito.spy(currentActiveBanner);

    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(banner));
    Mockito.when(bannerRepo.findActiveBanner())
      .thenReturn(Optional.of(currentActiveBanner));
    
    bannerService.setActiveBanner("1");

    assertFalse(currentActiveBanner.getActive());
    Mockito.verify(bannerRepo, times(2)).save(any(TemplateModel.class));
  }

  @Test
  public void willWillDefaultToFalseOnBannerCreate() {
    TemplateModel spyModel = Mockito.spy(TemplateModel.class);
    assertNull(spyModel.getActive());
    bannerService.createBanner(spyModel);

    assertFalse(spyModel.getActive());
    Mockito.verify(bannerRepo).save(spyModel);
  }
  
  @Test
  public void willUpdateBanner() {
    TemplateModel newBanner = TemplateModel.builder().title("1").id(1L).build();
    TemplateModel preExisting = TemplateModel.builder().title("title").active(false).id(1L).build();
    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(preExisting));

    bannerService.updateBanner(newBanner);

    Mockito.verify(bannerRepo, never()).save(newBanner);
    Mockito.verify(bannerRepo).save(preExisting);
    assertFalse(preExisting.getActive());
    assertEquals("1", preExisting.getTitle());
  }

  @Test
  public void willReturnBannerIfNoChangesToUpdate() {
    TemplateModel expected = TemplateModel.builder().title("1").id(1L).build();
    Mockito.when(bannerRepo.findById(1L))
      .thenReturn(Optional.of(expected));
    TemplateModel actual = bannerService.updateBanner(expected);

    assertEquals(expected, actual);
  }

  @Test
  public void willThrowExcetptionIfNullBannerIdSuppliedForUpdate() {
    assertThrows(IllegalArgumentException.class, () ->  bannerService.updateBanner(null));
  }

  @Test
  public void willReturnBannersIfData() {
    TemplateModel expected1 = TemplateModel.builder().title("1").build();
    TemplateModel expected2 = TemplateModel.builder().title("2").build();

    Mockito.when(bannerRepo.findAll())
      .thenReturn(List.of(expected1, expected2));
    List<TemplateModel> banners = bannerService.getAllBanners();
    
    assertEquals(2, banners.size());
    assertEquals(expected1, banners.get(0));
    assertEquals(expected2, banners.get(1));
  }

  @Test
  public void willReturnSingleBanner() {
    TemplateModel expected1 = TemplateModel.builder().title("1").build();
    Mockito.when(bannerRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.of(expected1));

    TemplateModel banner = bannerService.getBanner("1");

    assertEquals(expected1, banner);
  }

  @Test
  public void willThrowIfNoBannerPresent() {
    Mockito.when(bannerRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> bannerService.getBanner("1"));
  }
}
