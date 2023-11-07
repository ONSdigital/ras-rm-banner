package uk.gov.onsdigital.banner.repository;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import uk.gov.onsdigital.banner.model.TemplateModel;

public interface TemplateRepository extends DatastoreRepository<TemplateModel, Long> {
}
