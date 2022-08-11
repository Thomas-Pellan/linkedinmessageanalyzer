package fr.pellan.api.linkedinmessageanalyzer.db.repository;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.entity.MessageEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends ElasticsearchRepository<MessageEntity, String> {

    List<MessageEntity> findByDateAndConversation(LocalDateTime date, ConversationEntity conversation);
}
