package uk.gov.onsdigital.banner.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import uk.gov.onsdigital.banner.model.BannerModel;

public interface BannerRepository extends DatastoreRepository<BannerModel, String> {
}
