package uk.gov.onsdigital.banner.repository;

import org.springframework.stereotype.Repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import uk.gov.onsdigital.banner.model.BannerModel;

@Repository
public interface BannerRepository extends DatastoreRepository<BannerModel, String> {
}
