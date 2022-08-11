package fr.pellan.api.linkedinmessageanalyzer.service;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    public List<ConversationEntity> findByTitle(String title){

        return conversationRepository.findByTitleLike(title);
    }
}
