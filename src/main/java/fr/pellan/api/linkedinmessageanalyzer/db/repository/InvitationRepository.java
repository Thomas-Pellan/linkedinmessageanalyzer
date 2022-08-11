package fr.pellan.api.linkedinmessageanalyzer.db.repository;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.InvitationEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvitationRepository extends ElasticsearchRepository<InvitationEntity, String> {

    List<InvitationEntity> findByDate(LocalDateTime date);
}
