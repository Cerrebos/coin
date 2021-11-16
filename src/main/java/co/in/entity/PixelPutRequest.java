package co.in.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PixelPutRequest {

    private String hexColor;
    private String pixelId;

    public String toStringForRequest() {
        return "{" +
               "hexColor:" + "\"" + hexColor + "\" ,"  +
               "pixelId:" + "\"" + pixelId + "\" "  +
               "}";
    }
}
