package fr.pellan.api.linkedinmessageanalyzer.db.repository;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.MessageEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageRepository extends ElasticsearchRepository<MessageEntity, String> {

}
