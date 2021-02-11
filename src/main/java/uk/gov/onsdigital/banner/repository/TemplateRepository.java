package uk.gov.onsdigital.banner.repository;

import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import uk.gov.onsdigital.banner.model.TemplateModel;

public interface TemplateRepository extends DatastoreRepository<TemplateModel, Long> {
}
