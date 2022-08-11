package fr.pellan.api.linkedinmessageanalyzer.db.entity;

import fr.pellan.api.linkedinmessageanalyzer.enumeration.MessageDirection;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName="linkedin-invitation")
public class InvitationEntity {

    @Id
    private String id;

    @Field(type = FieldType.Nested)
    private PersonEntity from;

    @Field(type = FieldType.Nested)
    private PersonEntity to;

    @Field(type = FieldType.Date)
    private LocalDateTime date;

    @Field(type = FieldType.Text)
    private String message;

    @Field(type = FieldType.Text)
    private MessageDirection direction;
}
