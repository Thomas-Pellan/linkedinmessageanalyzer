package fr.pellan.api.linkedinmessageanalyzer.service;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.entity.InvitationEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.entity.MessageEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.entity.PersonEntity;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.ConversationRepository;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.InvitationRepository;
import fr.pellan.api.linkedinmessageanalyzer.db.repository.PersonRepository;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.InvitationDTO;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.MessageDirection;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEvent;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEventPublisher;
import fr.pellan.api.linkedinmessageanalyzer.factory.ConversationEntityFactory;
import fr.pellan.api.linkedinmessageanalyzer.factory.InvitationEntityFactory;
import fr.pellan.api.linkedinmessageanalyzer.factory.MessageEntityFactory;
import fr.pellan.api.linkedinmessageanalyzer.factory.PersonEntityFactory;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LinkedinFileParserService {

    @Autowired
    private ConversationEntityFactory conversationEntityFactory;

    @Autowired
    private PersonEntityFactory personEntityFactory;

    @Autowired
    private MessageEntityFactory messageEntityFactory;

    @Autowired
    private InvitationEntityFactory invitationEntityFactory;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private PersonRepository personRepository;

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

        //Removing empty content
        messages.removeIf(m -> StringUtils.isBlank(m.getContent()));

        //Group messages by conversations
        Map<String, List<MessageDTO>> dtos = messages.stream().collect(Collectors.groupingBy(m -> m.getConversationId()));

        dtos.forEach((id, msgList) -> indexConversationMessages(id, msgList));
    }

    private void indexConversationMessages(String conversationId, List<MessageDTO> messages) {

        //Avoiding blank conversations
        if(StringUtils.isBlank(conversationId) || CollectionUtils.isEmpty(messages)){
            return;
        }

        //Update conversation details
        ConversationEntity conversation = conversationEntityFactory.createOrMergeEntity(messages.get(0),
                conversationRepository.findByLinkedinId(conversationId).stream().findFirst().orElse(null));

        //Messages are stored in reverse order in the linkedin file
        updatePersonaInfo(conversation, messages.get(messages.size() - 1));

        List<MessageEntity> messagesToPersist = updateMessagesInfo(conversation, messages);

        conversation.setLastMessage(Collections.max(messagesToPersist, Comparator.comparing(MessageEntity::getDate)).getDate());
        conversation.setStarted(Collections.min(messagesToPersist, Comparator.comparing(MessageEntity::getDate)).getDate());
        conversation.setMessages(messagesToPersist);

        conversationRepository.save(conversation);
    }

    private List<MessageEntity> updateMessagesInfo(ConversationEntity conversation, List<MessageDTO> messages){

        List<MessageEntity> messagesToPersist = new ArrayList<>();
        messages.forEach(m -> {
            MessageEntity existingMsg = null;
            if(!CollectionUtils.isEmpty(conversation.getMessages())){

                existingMsg = conversation.getMessages()
                        .stream()
                        .filter(msg -> msg.getDate() != null && msg.getDate().compareTo(dateFormatUtil.getDateFromLinkedinMessage(m.getDate())) == 0)
                        .findFirst().orElse(null);
            }

            MessageEntity msg = messageEntityFactory.createOrMergeEntity(m, existingMsg);

            if(StringUtils.isBlank(m.getFrom()) || StringUtils.isBlank(m.getTo())){
                messagesToPersist.add(msg);
                return;
            }

            if(conversation.getFrom() != null && m.getFrom().equals(conversation.getFrom().getFullName())){
                msg.setDirection(MessageDirection.IN);
            } else {
                msg.setDirection(MessageDirection.OUT);
            }

            messagesToPersist.add(msg);
        });

        return messagesToPersist;
    }

    private void updatePersonaInfo(ConversationEntity conversation, MessageDTO firstMessage){

        PersonEntity sender = personRepository.findByFullName(firstMessage.getFrom()).stream().findFirst().orElse(null);
        PersonEntity receiver = personRepository.findByFullName(firstMessage.getTo()).stream().findFirst().orElse(null);

        sender = personEntityFactory.createorMergePerson(firstMessage, sender, true);
        receiver = personEntityFactory.createorMergePerson(firstMessage, receiver, false);

        conversation.setFrom(sender);
        conversation.setTo(receiver);
    }

    private void updatePersonaInfo(InvitationEntity invitation, InvitationDTO dto){

        PersonEntity sender = invitation.getFrom() == null ? null : personRepository.findByFullName(invitation.getFrom().getFullName()).stream().findFirst().orElse(null);
        PersonEntity receiver = invitation.getTo() == null ? null : personRepository.findByFullName(invitation.getTo().getFullName()).stream().findFirst().orElse(null);

        sender = personEntityFactory.createorMergePerson(dto, sender, true);
        receiver = personEntityFactory.createorMergePerson(dto, receiver, false);

        invitation.setFrom(sender);
        invitation.setTo(receiver);
    }

    private void importInvitations(MultipartFile file){

        List<InvitationDTO> invitations = csvParserUtil.parseCsvFile(file, InvitationDTO.class);

        if (CollectionUtils.isEmpty(invitations)) {
            return;
        }

        List<InvitationEntity> invitationsToPersist = new ArrayList<>();

        invitations.forEach(inv -> {

            InvitationEntity existingInvitation = invitationRepository.findByDate(dateFormatUtil.getDateFromLinkedinInvitation(inv.getSendAt())).stream().findFirst().orElse(null);
            InvitationEntity newInv = invitationEntityFactory.createorMergeEntity(inv, existingInvitation);

            updatePersonaInfo(newInv, inv);

            invitationsToPersist.add(newInv);
        });

        invitationsToPersist.removeIf(i -> i == null);

        invitationRepository.saveAll(invitationsToPersist);
    }
}
