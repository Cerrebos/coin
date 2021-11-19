package co.in.service;

import co.in.entity.Pixel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinService {

    public static final String FOULOSCOPIE_URL_PUT  = "https://api-flag.fouloscopie.com/pixel";

    private ImportDataService importDataService;

    @Autowired
    public CoinService(ImportDataService importDataService) {
        this.importDataService = importDataService;
    }

    public void drawOneDuck(String sessionId, int positionX, int positionY, String color1, String color2, boolean orientedRight) throws IOException {
        getPixelToChangeToMakeADuck(positionX, positionY, color1, color2, orientedRight)
                        .forEach(pixel ->  sendRequest(sessionId, pixel.getPixelId(), pixel.getColor()));
    }

    public void draw20DucksToTheRight(String sessionId, int positionX, int positionY, String color1, String color2, boolean orientedRight) throws IOException {
        for (int i = 0; i < 10; i++) {
            drawOneDuck(sessionId, positionX, positionY, color1, color2, orientedRight);
            positionX += 7;
        }
    }

    public void draw20DucksToTheLeft(String sessionId, int positionX, int positionY, String color1, String color2, boolean orientedRight) throws IOException {
        for (int i = 0; i < 10; i++) {
            drawOneDuck(sessionId, positionX, positionY, color1, color2, orientedRight);
            positionX -= 7;
        }
    }

    public void drawOneImage(String sessionIdTextField, int positionX, int positionY, File selectedFile) throws IOException {
        List<Pixel> pixelsCoordinatesAndColorToChange = getPixelListFromImage(selectedFile).stream()
                .peek(pixel -> {
                    pixel.setXpos(pixel.getXpos() + positionX);
                    pixel.setYpos(pixel.getYpos() + positionY);
                })
                .collect(Collectors.toList());
        getPixelToChangeToMakeAnImage(pixelsCoordinatesAndColorToChange)
                        .forEach(pixel ->  sendRequest(sessionIdTextField, pixel.getPixelId(), pixel.getColor()));
    }

    private void sendRequest(String sessionId, String pixelId, String color) {

        //generate request with sessionId in header
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", sessionId);
        HttpEntity<String> entity = new HttpEntity<>("{\"hexColor\":\""+ color + "\",\"pixelId\":\"" + pixelId + "\"}", headers);

        //meh il est tard
        try {
            ResponseEntity<String> exchange = restTemplate.exchange(FOULOSCOPIE_URL_PUT, HttpMethod.PUT, entity, String.class);
            System.out.println(Instant.now().toString() + " Fouloscopie API returned " + exchange.getStatusCode() + " : updated pixel with id " + pixelId + " to color " + color);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                Thread.sleep(1000 * 63 * 2);
            } catch (InterruptedException e) {
                System.out.println("ça n'a pas marché");
            }
        }
    }

    private List<Pixel> getPixelToChangeToMakeADuck(int xOfDuckHead, int yOfDuckHead, String color1, String color2, boolean orientedRight) throws IOException {

        return importDataService.getAllPixels().stream()
                .filter(pixel -> orientedRight ? isPixelForDuckToTheRight(xOfDuckHead, yOfDuckHead, pixel) : isPixelForDuckToTheLeft(xOfDuckHead, yOfDuckHead, pixel))
                .peek(pixel -> {
                    if (orientedRight) {
                        modifyColorForDuckToTheRight(xOfDuckHead, yOfDuckHead, color1, color2, pixel);
                    } else {
                        modifyColorForDuckToTheLeft(xOfDuckHead, yOfDuckHead, color1, color2, pixel);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<Pixel> getPixelToChangeToMakeAnImage(List<Pixel> pixelsCoordinatesAndColor) throws IOException {
        return importDataService.getAllPixels().stream()
                .filter(pixelsCoordinatesAndColor::contains)
                .peek(completePixel ->
                        pixelsCoordinatesAndColor.stream()
                                .filter(p -> p.equals(completePixel))
                                .findAny().ifPresent(p -> completePixel.setColor(p.getColor()))
                )
                .collect(Collectors.toList());
    }

    private void modifyColorForDuckToTheLeft(int xOfDuckHead, int yOfDuckHead, String color1, String color2, Pixel pixel) {
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead)      pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead - 1)    && pixel.getYpos() == yOfDuckHead)      pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead - 1)    && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color2);// color2
        if (pixel.getXpos() == (xOfDuckHead - 2)    && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color2);// color2
        if (pixel.getXpos() == (xOfDuckHead + 2)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead + 1)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead - 1)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead + 1)    && pixel.getYpos() == yOfDuckHead + 3)  pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 3)  pixel.setColor(color1);
    }

    private boolean isPixelForDuckToTheLeft(int xOfDuckHead, int yOfDuckHead, Pixel pixel) {
        return (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead)      ||
                (pixel.getXpos() == (xOfDuckHead - 1)   && pixel.getYpos() == yOfDuckHead)      ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead - 1)   && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead - 2)   && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead + 2)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead + 1)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead - 1)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead + 1)   && pixel.getYpos() == yOfDuckHead + 3)  ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 3);
    }

    private void modifyColorForDuckToTheRight(int xOfDuckHead, int yOfDuckHead, String color1, String color2, Pixel pixel) {
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead)      pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead + 1)    && pixel.getYpos() == yOfDuckHead)      pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead + 1)    && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color2);// color2
        if (pixel.getXpos() == (xOfDuckHead + 2)    && pixel.getYpos() == yOfDuckHead + 1)  pixel.setColor(color2);// color2
        if (pixel.getXpos() == (xOfDuckHead - 2)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead - 1)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead + 1)    && pixel.getYpos() == yOfDuckHead + 2)  pixel.setColor(color1);
        if (pixel.getXpos() == (xOfDuckHead - 1)    && pixel.getYpos() == yOfDuckHead + 3)  pixel.setColor(color1);
        if (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 3)  pixel.setColor(color1);
    }

    private boolean isPixelForDuckToTheRight(int xOfDuckHead, int yOfDuckHead, Pixel pixel) {
        return (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead)      ||
                (pixel.getXpos() == (xOfDuckHead + 1)   && pixel.getYpos() == yOfDuckHead)      ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead + 1)   && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead + 2)   && pixel.getYpos() == yOfDuckHead + 1)  ||
                (pixel.getXpos() == (xOfDuckHead - 2)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead - 1)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead + 1)   && pixel.getYpos() == yOfDuckHead + 2)  ||
                (pixel.getXpos() == (xOfDuckHead - 1)   && pixel.getYpos() == yOfDuckHead + 3)  ||
                (pixel.getXpos() == xOfDuckHead && pixel.getYpos() == yOfDuckHead + 3);
    }


    private List<Pixel> getPixelListFromImage(File file) throws IOException
    {
        // Load the image into memory
        if (file == null) {
            file = new ClassPathResource("coin.png").getFile();
        }

        BufferedImage image = ImageIO.read(file);

        // Instantiate a pixel list that will contain the XY and colour of each non-empty pixel in the image
        List<Pixel> pixelList = new ArrayList<>();
        // Go through every pixel in the height*width space defined by the image
        for(int y = 0 ; y < image.getHeight() ; y++)
        {
            for( int x = 0 ; x < image.getWidth() ; x++)
            {
                // Check if the pixel is transparent
                if( image.getRGB(x,y)>>24 == 0x00)
                    // If it is, then ignore it by going to the next iteration of the loop
                    continue;

                // Get the pixel colour information
                Color pixelColor = new Color(image.getRGB(x, y));

                // Add a pixel to the list with positions and colour specified, other fields left empty
                pixelList.add(new Pixel(0, 0,
                        // Positions = pixel coordinates in the image frame of reference
                        x, y,
                        // Colour = hex code built from the RGB values of the pixel
                        String.format("#%02x%02x%02x", pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue()),
                        "", "", ""));
            }
        }

        return pixelList;
    }

}
