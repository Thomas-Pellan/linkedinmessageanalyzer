package fr.pellan.api.linkedinmessageanalyzer.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName="linkedin")
public class ConversationEntity {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String linkedinId;

    @Field(type = FieldType.Date)
    private LocalDateTime started;

    @Field(type = FieldType.Date)
    private LocalDateTime lastMessage;

    @Field(type = FieldType.Boolean)
    private boolean initiatedByYou;
}
