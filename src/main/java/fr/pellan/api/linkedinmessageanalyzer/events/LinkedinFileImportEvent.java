package fr.pellan.api.linkedinmessageanalyzer.events;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description of the linkedin file import event.
 */
@Getter
public class LinkedinFileImportEvent extends ApplicationEvent {

    /**
     * The import type.
     */
    private ImportType type;

    /**
     * The file info used for import.
     */
    private MultipartFile file;

    /***
     * Constructor for the event.
     * @param source the event source
     * @param file the event data
     */
    public LinkedinFileImportEvent(Object source, ImportType type, MultipartFile file) {
        super(source);
        this.file = file;
        this.type = type;
    }
}
