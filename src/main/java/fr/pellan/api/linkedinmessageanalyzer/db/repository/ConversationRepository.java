package fr.pellan.api.linkedinmessageanalyzer.db.repository;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ConversationRepository extends ElasticsearchRepository<ConversationEntity, String> {

    List<ConversationEntity> findByLinkedinId(String linkedinId);

    List<ConversationEntity> findByTitle(String title);
}
