package fr.pellan.api.linkedinmessageanalyzer.service;

import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.InvitationDTO;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEvent;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEventPublisher;
import fr.pellan.api.linkedinmessageanalyzer.util.CsvParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class LinkedinFileParserService {

    @Autowired
    private CsvParserUtil csvParserUtil;

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
    }

    private void importInvitations(MultipartFile file){

        List<InvitationDTO> invitations = csvParserUtil.parseCsvFile(file, InvitationDTO.class);
    }
}
