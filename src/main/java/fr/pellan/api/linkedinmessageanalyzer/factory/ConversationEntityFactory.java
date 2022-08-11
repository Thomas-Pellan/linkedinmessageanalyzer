package fr.pellan.api.linkedinmessageanalyzer.factory;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConversationEntityFactory {

    public ConversationEntity createOrMergeEntity(MessageDTO message, ConversationEntity conversation){

        if(message == null){
            return conversation;
        }

        ConversationEntity newConv = new ConversationEntity();
        if(conversation != null){
            newConv = conversation;
        }

        newConv.setLinkedinId(message.getConversationId());
        newConv.setTitle(message.getConversationTitle());
        newConv.setFrom(message.getFrom());
        newConv.setSubject(message.getSubject());
        newConv.setTo(message.getTo());

        return newConv;
    }
}
