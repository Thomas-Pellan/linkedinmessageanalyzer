package fr.pellan.api.linkedinmessageanalyzer.factory;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.PersonEntity;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.InvitationDTO;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonEntityFactory {

    public PersonEntity createorMergePerson(MessageDTO message, PersonEntity existingPerson, boolean isSender){

        if(message == null){
            return existingPerson;
        }

        PersonEntity newPerson = new PersonEntity();
        if(existingPerson != null){
            newPerson = existingPerson;
        }

        newPerson.setFullName(isSender ? message.getFrom(): message.getTo());
        if(isSender){
            newPerson.setLinkedinProfileUrl(message.getSenderProfileUrl());
        }

        return newPerson;
    }

    public PersonEntity createorMergePerson(InvitationDTO invitation, PersonEntity existingPerson, boolean isSender){

        if(invitation == null){
            return existingPerson;
        }

        PersonEntity newPerson = new PersonEntity();
        if(existingPerson != null){
            newPerson = existingPerson;
        }

        newPerson.setFullName(isSender ? invitation.getFrom(): invitation.getTo());

        return newPerson;
    }
}
