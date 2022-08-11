package fr.pellan.api.linkedinmessageanalyzer.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(indexName="linkedin-conversation")
public class ConversationEntity {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String linkedinId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Nested)
    private PersonEntity from;

    @Field(type = FieldType.Nested)
    private PersonEntity to;

    @Field(type = FieldType.Date)
    private LocalDateTime started;

    @Field(type = FieldType.Date)
    private LocalDateTime lastMessage;

    @Field(type = FieldType.Text)
    private String subject;

    @Field( type = FieldType.Nested)
    private List<MessageEntity> messages;
}
