package uk.gov.onsdigital.banner.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.repository.TemplateRepository;

@ExtendWith(MockitoExtension.class)
public class TemplateServiceUnitTest {
  
  @InjectMocks
  private TemplateService templateService;

  @Mock
  private TemplateRepository templateRepo;

  @Test
  public void willReturnBannerIfNoChangesToUpdate() {
    TemplateModel expected = TemplateModel.builder().title("1").id(1L).build();
    Mockito.when(templateRepo.findById(1L))
      .thenReturn(Optional.of(expected));
    TemplateModel actual = templateService.updateTemplate(expected);

    assertEquals(expected, actual);
  }

  @Test
  public void willThrowExcetptionIfNullBannerIdSuppliedForUpdate() {
    assertThrows(IllegalArgumentException.class, () ->  templateService.updateTemplate(null));
  }

  @Test
  public void willReturnBannersIfData() {
    TemplateModel expected1 = TemplateModel.builder().title("1").build();
    TemplateModel expected2 = TemplateModel.builder().title("2").build();

    Mockito.when(templateRepo.findAll())
      .thenReturn(List.of(expected1, expected2));
    List<TemplateModel> banners = templateService.getAllTemplates();
    
    assertEquals(2, banners.size());
    assertEquals(expected1, banners.get(0));
    assertEquals(expected2, banners.get(1));
  }

  @Test
  public void willReturnSingleBanner() {
    TemplateModel expected1 = TemplateModel.builder().title("1").build();
    Mockito.when(templateRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.of(expected1));

    TemplateModel banner = templateService.getTemplate("1");

    assertEquals(expected1, banner);
  }

  @Test
  public void willThrowIfNoBannerPresent() {
    Mockito.when(templateRepo.findById(Long.valueOf("1")))
      .thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> templateService.getTemplate("1"));
  }
}
