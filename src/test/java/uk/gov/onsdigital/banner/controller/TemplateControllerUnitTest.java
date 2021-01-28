package uk.gov.onsdigital.banner.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.onsdigital.banner.model.TemplateModel;
import uk.gov.onsdigital.banner.service.BannerService;
import uk.gov.onsdigital.banner.service.TemplateService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TemplateControllerUnitTest {
  
  @InjectMocks
  private TemplateController templateController;

  @Mock
  private TemplateService templateService;

  @Test
  public void willReturn200() {
    ResponseEntity<List<TemplateModel>> resp = templateController.getTemplates();

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturnEmptyListIfNoData() {
    ResponseEntity<List<TemplateModel>> resp = templateController.getTemplates();

    assertEquals(0, resp.getBody().size());
  }

  @Test
  public void willReturnTemplatesIfData() {
    ResponseEntity<List<TemplateModel>> resp = templateController.getTemplates();
    
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturnSingleTemplate() {
    ResponseEntity<TemplateModel> resp = templateController.getTemplate("1");

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturn404IfNoTemplateFound() {
    Mockito.when(templateService.getTemplate("1"))
      .thenThrow(new NoSuchElementException());
    ResponseEntity<TemplateModel> resp = templateController.getTemplate("1");

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumber() {
    Mockito.when(templateService.getTemplate("abc"))
      .thenThrow(new NumberFormatException());
    ResponseEntity<TemplateModel> resp = templateController.getTemplate("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willCreateTemplate() {
    ResponseEntity<TemplateModel> resp = templateController.createTemplate(new TemplateModel());

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  public void willRemoveBanner() {
    ResponseEntity<TemplateModel> resp = templateController.removeTemplate("1");

    assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
  }

  @Test
  public void willReturnBadRequestIfPathVariableIsNotNumberOnDelete() {
    Mockito.doThrow(new NumberFormatException())
      .when(templateService)
      .removeTemplate("abc");
    ResponseEntity<TemplateModel> resp = templateController.removeTemplate("abc");

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  public void willUpdateBanner() {
    TemplateModel newBanner = TemplateModel.builder().title("1").id(1L).build();
    Mockito.when(templateService.updateTemplate(newBanner))
      .thenReturn(newBanner);

    ResponseEntity<TemplateModel> resp = templateController.updateTemplate(newBanner);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void willReturn400IfNullBannerSuppliedForUpdate() {
    Mockito.when(templateService.updateTemplate(null))
      .thenThrow(new IllegalArgumentException());
    ResponseEntity<TemplateModel> resp = templateController.updateTemplate(null);

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }
}
