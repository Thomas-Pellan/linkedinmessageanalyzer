package fr.pellan.api.linkedinmessageanalyzer.factory;

import fr.pellan.api.linkedinmessageanalyzer.db.entity.InvitationEntity;
import fr.pellan.api.linkedinmessageanalyzer.dto.linkedin.InvitationDTO;
import fr.pellan.api.linkedinmessageanalyzer.enumeration.MessageDirection;
import fr.pellan.api.linkedinmessageanalyzer.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvitationEntityFactory {

    @Autowired
    private DateFormatUtil dateFormatUtil;

    private static final String OUT= "OUTGOING";

    public InvitationEntity createorMergeEntity(InvitationDTO dto, InvitationEntity invitation){

        if(dto == null){
            return invitation;
        }

        InvitationEntity newInv = new InvitationEntity();
        if(invitation != null){
            newInv = invitation;
        }

        newInv.setDate(dateFormatUtil.getDateFromLinkedinInvitation(dto.getSendAt()));
        newInv.setMessage(dto.getMsg());
        newInv.setDirection(!StringUtils.isBlank(dto.getDirection()) && OUT.equals(dto.getDirection()) ? MessageDirection.OUT : MessageDirection.IN);

        return newInv;
    }
}
