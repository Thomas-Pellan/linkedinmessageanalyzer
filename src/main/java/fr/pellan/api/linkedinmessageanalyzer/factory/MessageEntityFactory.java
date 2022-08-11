package fr.pellan.api.linkedinmessageanalyzer.factory;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.MessageEntity;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import fr.pellan.api.linkedinmessageanalyzer.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class MessageEntityFactory {

    @Autowired
    private DateFormatUtil dateFormatUtil;

    public MessageEntity createOrMergeEntity(MessageDTO dto, MessageEntity message){

        if(dto == null){
            return message;
        }

        MessageEntity newMsg = new MessageEntity();
        if(message != null){
            newMsg = message;
        }

        newMsg.setContent(dto.getContent());
        try {
            newMsg.setDate(dateFormatUtil.getDateFromLinkedinMessage(dto.getDate()));
        } catch (DateTimeParseException e) {
            log.error("createOrMergeEntity : invalid date detected on conversation id {}", dto.getConversationId(),e);
        }
        newMsg.setContent(dto.getContent());

        return newMsg;
    }
}
