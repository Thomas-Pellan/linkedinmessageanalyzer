package fr.pellan.api.linkedinmessageanalyzer.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LinkedinConnectorException extends RuntimeException{

    private final String message;
}
