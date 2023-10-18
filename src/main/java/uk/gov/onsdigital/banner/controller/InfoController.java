package uk.gov.onsdigital.banner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/info")
public class InfoController {

  @GetMapping("")
  public ResponseEntity<Void> getInfo() {
    return ResponseEntity.ok().build();
  }
}
