package co.in.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return xpos == pixel.xpos && ypos == pixel.ypos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xpos, ypos);
    }
}
