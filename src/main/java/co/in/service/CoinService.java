package co.in.service;

import co.in.entity.Pixel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.io.IOException;
import java.time.Instant;
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

    public void draw20DucksToTheRight(String sessionIdTextField, int positionXTextField, int positionYTextField, String color1, String color2, boolean orientedRight) throws IOException {
        for (int i = 0; i < 10; i++) {
            drawOneDuck(sessionIdTextField, positionXTextField, positionYTextField, color1, color2, orientedRight);
            positionXTextField += 7;
        }
    }

    public void draw20DucksToTheLeft(String sessionIdTextField, int positionXTextField, int positionYTextField, String color1, String color2, boolean orientedRight) throws IOException {
        for (int i = 0; i < 10; i++) {
            drawOneDuck(sessionIdTextField, positionXTextField, positionYTextField, color1, color2, orientedRight);
            positionXTextField -= 7;
        }
    }

    public void drawOneDuck(String sessionIdTextField, int positionXTextField, int positionYTextField, String color1, String color2, boolean orientedRight) throws IOException {
        getPixelToChangeToMakeADuck(positionXTextField, positionYTextField, color1, color2, orientedRight)
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

}
