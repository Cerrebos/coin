package co.in.entity;

import com.opencsv.bean.CsvBindByName;
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
@Document(indexName = "pixelfromcsv")
public class PixelFromCSV {

    @CsvBindByName
    @Id
    int index;
    @CsvBindByName
    @Field(type = FieldType.Integer)
    int indexInFlag;
    @CsvBindByName
    @Field(type = FieldType.Integer)
    int xpos;
    @CsvBindByName
    @Field(type = FieldType.Integer)
    int ypos;
    @CsvBindByName
    @Field(type = FieldType.Keyword)
    String color;
    @CsvBindByName
    @Field(type = FieldType.Keyword)
    String author;
    @CsvBindByName
    @Field(type = FieldType.Keyword)
    String pseudo;
    @CsvBindByName
    @Field(type = FieldType.Keyword)
    String pixelId;


}
