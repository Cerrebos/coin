package co.in.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "pixel")
public class Pixel {

    @Id
    int index;
    @Field(type = FieldType.Integer)
    int x;
    @Field(type = FieldType.Integer)
    int y;
    @Field(type = FieldType.Integer)
    int indexInFlag;
    @Field(type = FieldType.Keyword)
    String hexColor;
    @Field(type = FieldType.Keyword)
    String author;
    @Field(type = FieldType.Keyword)
    String pseudo;
    @Field(type = FieldType.Keyword)
    String entityId;

}
