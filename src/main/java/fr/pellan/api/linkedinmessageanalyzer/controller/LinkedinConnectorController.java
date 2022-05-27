package fr.pellan.api.linkedinmessageanalyzer.controller;

import fr.pellan.api.linkedinmessageanalyzer.service.LinkedinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("linkedin-connect")
public class LinkedinConnectorController {

@Autowired
private LinkedinService linkedinService;

    @GetMapping(value = "/init")
    public ResponseEntity<String> getLinkedinConnectUrl() {

        return new ResponseEntity(linkedinService.getLinkedinConnectUrl(), HttpStatus.OK);
    }

    @GetMapping(value = "/execute")
    public ResponseEntity<String> executeLinkedinCommand(@RequestParam(name="code") String code){

        return new ResponseEntity(linkedinService.getUserEmail(code), HttpStatus.OK);
    }
}
