package fr.pellan.api.linkedinmessageanalyzer.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName="linkedin")
public class MessageEntity {

    @Id
    private String id;

    private ConversationEntity conversation;

    @Field(type = FieldType.Date)
    private LocalDateTime date;

    private PersonEntity from;

    private PersonEntity to;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;
}
