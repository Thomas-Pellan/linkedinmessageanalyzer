package fr.pellan.api.linkedinmessageanalyzer.events;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Event publisher for the linledin file import event.
 */
@Component
public class LinkedinFileImportEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publishes a file import event.
     * @param file the event target file
     */
    public void publishImportFileEvent(final MultipartFile file, final ImportType type) {

        LinkedinFileImportEvent event = new LinkedinFileImportEvent(this, type, file);
        applicationEventPublisher.publishEvent(event);
    }
}
