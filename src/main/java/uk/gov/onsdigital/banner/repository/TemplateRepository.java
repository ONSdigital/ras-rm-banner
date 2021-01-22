package uk.gov.onsdigital.banner.repository;

import java.util.Optional;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.cloud.gcp.data.datastore.repository.query.Query;
import uk.gov.onsdigital.banner.model.TemplateModel;

public interface TemplateRepository extends DatastoreRepository<TemplateModel, Long> {

  @Query("SELECT * FROM Banner where active = True")
  Optional<TemplateModel> findActiveBanner();
}
