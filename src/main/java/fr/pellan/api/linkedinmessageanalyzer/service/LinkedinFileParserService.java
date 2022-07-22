package fr.pellan.api.linkedinmessageanalyzer.service;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEvent;
import fr.pellan.api.linkedinmessageanalyzer.events.LinkedinFileImportEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class LinkedinFileParserService {

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

    }
}
