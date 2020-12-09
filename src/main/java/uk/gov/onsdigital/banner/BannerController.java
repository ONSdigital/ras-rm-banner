package uk.gov.onsdigital.banner;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
public class BannerController {

  @Autowired
  private BannerRepository bannerRepo;
  
  @GetMapping("")
  public ResponseEntity<List<BannerModel>> getBanners() {
    Iterator<BannerModel> bannerIter = bannerRepo.findAll().iterator();
    return ResponseEntity.ok()
      .body(IteratorUtils.toList(bannerIter));
  }

  @GetMapping("/{title}")
  public ResponseEntity<BannerModel> getBanner(
      @PathVariable("title") String title) {
    return bannerRepo.findById(title)
        .map(t -> ResponseEntity.ok().body(t))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
