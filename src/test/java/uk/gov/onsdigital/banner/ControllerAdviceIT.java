package uk.gov.onsdigital.banner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.onsdigital.banner.controller.BannerController;
import uk.gov.onsdigital.banner.repository.BannerRepository;
import uk.gov.onsdigital.banner.service.BannerService;
import uk.gov.onsdigital.banner.service.TemplateService;

@WebMvcTest(value = BannerController.class)
public class ControllerAdviceIT {
  
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TemplateService templateService;

  @MockBean
  private BannerService bannerService;

  @MockBean
  private BannerRepository bannerRepository;
  
  @Test
  public void willThrow500OnRuntimeException() throws Exception {
    RequestBuilder requestBuilder = MockMvcRequestBuilders
      .get("/template/1")
      .accept(MediaType.APPLICATION_JSON);

    Mockito.when(templateService.getTemplate("1"))
      .thenThrow(new IllegalStateException());
    
    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
    assertEquals("The service has thrown an unexpected runtime exception", result.getResponse().getContentAsString());
  }
}
