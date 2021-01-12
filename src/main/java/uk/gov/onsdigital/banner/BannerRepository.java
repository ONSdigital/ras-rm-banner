package uk.gov.onsdigital.banner;

import java.util.Optional;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.cloud.gcp.data.datastore.repository.query.Query;

public interface BannerRepository extends DatastoreRepository<BannerModel, Long> {

  @Query("SELECT * FROM Banner where active = True")
  Optional<BannerModel> findActiveBanner();
}
