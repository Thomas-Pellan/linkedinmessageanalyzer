package fr.pellan.api.linkedinmessageanalyzer.controller;

import fr.pellan.api.linkedinmessageanalyzer.service.LinkedinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("linkedin-connect")
public class LinkedinConnectorController {

    @Autowired
    private LinkedinService linkedinService;

    @GetMapping(value = "/init")
    public ModelAndView getLinkedinConnectUrl(@RequestParam(name="callback") String callback) {

        return new ModelAndView("redirect:" + linkedinService.getLinkedinConnectUrl(callback));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLinkedinUserProfile(@RequestParam(name="code") String code){

        return new ResponseEntity<>(linkedinService.getUserProfile(code), HttpStatus.OK);
    }

    @GetMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLinkedinUserEmail(@RequestParam(name="code") String code){

        return new ResponseEntity<>(linkedinService.getUserEmail(code), HttpStatus.OK);
    }

    @GetMapping(value = "/messages")
    public ResponseEntity<String> executeLinkedinCommand(@RequestParam(name="code") String code){

        return new ResponseEntity<>(linkedinService.getUserMessages(code), HttpStatus.OK);
    }
}
