package fr.pellan.api.linkedinmessageanalyzer.controller;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.ImportType;
import fr.pellan.api.linkedinmessageanalyzer.service.LinkedinFileParserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("linkedin-content-parser")
public class LinkedinMessageFileParserController {

    @Autowired
    private LinkedinFileParserService linkedinFileParserService;

    private static final String ACCEPTED_FILE_EXTENSION = "csv";

    @PostMapping(value = "/messages")
    public ResponseEntity extractLinkedinMessagesData(@RequestBody MultipartFile file) {

        ResponseEntity checkResponse = validateQuery(file);
        if(checkResponse != null){
            return checkResponse;
        }

        HttpStatus responseStatus = linkedinFileParserService.publishFileParsingEvent(file, ImportType.MESSAGES) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity(responseStatus);
    }

    @PostMapping(value = "/invitations")
    public ResponseEntity extractLinkedinInvitationsData(@RequestBody MultipartFile file) {

        ResponseEntity checkResponse = validateQuery(file);
        if(checkResponse != null){
            return checkResponse;
        }

        HttpStatus responseStatus = linkedinFileParserService.publishFileParsingEvent(file, ImportType.INVITATIONS) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity(responseStatus);
    }

    private ResponseEntity validateQuery(MultipartFile file){

        if(file == null || file.isEmpty()){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        if(!ACCEPTED_FILE_EXTENSION.equals(FilenameUtils.getExtension(file.getOriginalFilename()))){
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        return null;
    }
}
