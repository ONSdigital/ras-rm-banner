package uk.gov.onsdigital.banner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.onsdigital.banner.model.BannerModel;
import uk.gov.onsdigital.banner.service.BannerService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/info")
public class InfoController {
  
  @GetMapping("")
  public ResponseEntity<Void> getInfo() {
      return ResponseEntity.ok().build();
  }
}
