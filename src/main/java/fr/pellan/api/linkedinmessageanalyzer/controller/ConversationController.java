package fr.pellan.api.linkedinmessageanalyzer.controller;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.ConversationEntity;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.MessageDirection;
import fr.pellan.api.linkedinmessageanalyzer.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping(value = "/title")
    public ResponseEntity<List<ConversationEntity>> getConversationsByTitle(@RequestParam(value = "query")
                                                                 String query){

        return new ResponseEntity(conversationService.findByTitle(query), HttpStatus.OK);
    }
}
