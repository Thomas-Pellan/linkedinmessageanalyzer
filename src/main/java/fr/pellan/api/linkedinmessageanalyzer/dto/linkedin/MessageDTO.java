package fr.pellan.api.linkedinmessageanalyzer.dto.linkedin;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class MessageDTO {

    @CsvBindByName(column = "CONVERSATION ID")
    private String conversationId;

    @CsvBindByName(column = "CONVERSATION TITLE")
    private String conversationTitle;

    @CsvBindByName(column = "FROM")
    private String from;

    @CsvBindByName(column = "SENDER PROFILE URL")
    private String senderProfileUrl;

    @CsvBindByName(column = "TO")
    private String to;

    @CsvBindByName(column = "DATE")
    private String date;

    @CsvBindByName(column = "SUBJECT")
    private String subject;

    @CsvBindByName(column = "CONTENT")
    private String content;
}
