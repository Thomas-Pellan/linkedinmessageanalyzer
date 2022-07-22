package fr.pellan.api.linkedinmessageanalyzer.dto.linkedin;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class InvitationDTO {

    @CsvBindByName(column = "from")
    private String from;

    @CsvBindByName(column = "to")
    private String to;

    @CsvBindByName(column = "Send at")
    private String sendAt;

    @CsvBindByName(column = "Message")
    private String msg;

    @CsvBindByName(column = "Direction")
    private String direction;
}
