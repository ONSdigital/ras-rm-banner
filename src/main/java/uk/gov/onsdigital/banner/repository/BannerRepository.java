package uk.gov.onsdigital.banner.repository;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import uk.gov.onsdigital.banner.model.BannerModel;

public interface BannerRepository extends DatastoreRepository<BannerModel, String> {
}
