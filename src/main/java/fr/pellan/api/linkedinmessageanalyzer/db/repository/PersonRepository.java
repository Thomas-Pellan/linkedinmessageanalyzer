package fr.pellan.api.linkedinmessageanalyzer.db.repository;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.PersonEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PersonRepository extends ElasticsearchRepository<PersonEntity, String> {

    List<PersonEntity> findByFullName(String fullName);
}
