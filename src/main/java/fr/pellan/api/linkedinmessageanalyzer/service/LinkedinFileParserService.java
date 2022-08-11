package fr.pellan.api.linkedinmessageanalyzer.service;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.entity.MessageEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.ConversationRepository;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.MessageRepository;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.InvitationDTO;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEvent;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEventPublisher;
import fr.pellan.api.linkedinmessageanalyzer.factory.ConversationEntityFactory;
import fr.pellan.api.linkedinmessageanalyzer.factory.MessageEntityFactory;
import fr.pellan.api.linkedinmessageanalyzer.util.CsvParserUtil;
import fr.pellan.api.linkedinmessageanalyzer.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LinkedinFileParserService {

    @Autowired
    private ConversationEntityFactory conversationEntityFactory;

    @Autowired
    private MessageEntityFactory messageEntityFactory;


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private CsvParserUtil csvParserUtil;

    @Autowired
    private DateFormatUtil dateFormatUtil;

    @Autowired
    private LinkedinFileImportEventPublisher linkedinFileImportEventPublisher;

    public boolean publishFileParsingEvent(MultipartFile file, ImportType type){

        linkedinFileImportEventPublisher.publishImportFileEvent(file, type);

        return true;
    }

    /**
     * Async event, used to trigger a file import using file data.
     * @param event the data of the file to import.
     */
    @Async
    @EventListener
    public void importFile(LinkedinFileImportEvent event){

        if(event == null){
            log.error("importFile : empty event");
            return;
        }

        switch (event.getType()){
            case MESSAGES -> importMessages(event.getFile());
            default ->importInvitations(event.getFile());
        }
    }

    private void importMessages(MultipartFile file){

        List<MessageDTO> messages = csvParserUtil.parseCsvFile(file, MessageDTO.class);

        if(CollectionUtils.isEmpty(messages)){
            return;
        }

        //Group messages by conversations
        Map<String, List<MessageDTO>> dtos = messages.stream().collect(Collectors.groupingBy(m -> m.getConversationId()));

        dtos.forEach((id, msgList) -> indexConversationMessages(id, msgList));
    }

    private void indexConversationMessages(String conversationId, List<MessageDTO> messages) {

        if(StringUtils.isBlank(conversationId) || CollectionUtils.isEmpty(messages)){
            return;
        }

        ConversationEntity conversation = conversationEntityFactory.createOrMergeEntity(messages.get(0),
                conversationRepository.findByLinkedinId(conversationId).stream().findFirst().orElse(null));

        ConversationEntity persistedConv = conversationRepository.save(conversation);

        if(persistedConv == null){
            log.error("indexConversationMessages : error during conversation persistence (conv id {}), will not save the related messages", conversationId);
            return;
        }

        List<MessageEntity> messagesToPersist = new ArrayList<>();

        messages.forEach(m -> {
            List<MessageEntity> msgs = messageRepository.findByDateAndConversation(dateFormatUtil.getDateFromLinkedinString(m.getDate()), persistedConv);
            MessageEntity existingMsg = null;
            if(!CollectionUtils.isEmpty(msgs)){
                existingMsg = msgs.get(0);
            }

            MessageEntity msg = messageEntityFactory.createOrMergeEntity(m, existingMsg);
            messagesToPersist.add(msg);
        });

        if(CollectionUtils.isEmpty(messagesToPersist)){
            return;
        }
        messageRepository.saveAll(messagesToPersist);
    }

    private void importInvitations(MultipartFile file){

        List<InvitationDTO> invitations = csvParserUtil.parseCsvFile(file, InvitationDTO.class);
    }
}
