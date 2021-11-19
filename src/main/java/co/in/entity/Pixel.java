package co.in.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pixel {

    @CsvBindByName
    int index;
    @CsvBindByName
    int indexInFlag;
    @CsvBindByName
    int xpos;
    @CsvBindByName
    int ypos;
    @CsvBindByName
    String color;
    @CsvBindByName
    String author;
    @CsvBindByName
    String pseudo;
    @CsvBindByName
    String pixelId;


}
