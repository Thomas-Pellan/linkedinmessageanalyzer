package fr.pellan.api.linkedinmessageanalyzer.db.entity;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.MessageDirection;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
@Data
@Document(indexName="linkedin-message")
public class MessageEntity {

    @Id
    private String id;

    @Field(type = FieldType.Date)
    private LocalDateTime date;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private MessageDirection direction;
}
