package uk.gov.onsdigital.banner;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;

public interface BannerRepository extends DatastoreRepository<BannerModel, Long> {
  
}
