package co.in.service;

import co.in.entity.Pixel;
import co.in.entity.PixelFromCSV;
import co.in.repository.PixelFromCSVRepository;
import co.in.repository.PixelRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

@ShellComponent
public class CoinService {

    public static final String FOULOSCOPIE_URL_PUT  = "https://api-flag.fouloscopie.com/pixel";

    private final PixelFromCSVRepository pixelRepository;

    @Autowired
    public CoinService(PixelFromCSVRepository pixelRepository) {
        this.pixelRepository = pixelRepository;
    }

    @ShellMethod("dessine moi un canard")
    public void coin(String sessionId, int x, int y) {
        getPixelToChangeToMakeADuck(x, y)
                        .forEach(pixel ->  sendRequest(sessionId, pixel.getPixelId(), pixel.getColor()));
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
                System.out.println("meh");
            }
        }
    }


    private List<PixelFromCSV> getPixelToChangeToMakeADuck(int XOfDuckHead, int YOfDuckHead) {

        PixelFromCSV head1 = pixelRepository.findByXposAndYpos(XOfDuckHead, YOfDuckHead);
        PixelFromCSV head2 = pixelRepository.findByXposAndYpos(XOfDuckHead + 1, YOfDuckHead);
        PixelFromCSV head3 = pixelRepository.findByXposAndYpos(XOfDuckHead, YOfDuckHead + 1);
        PixelFromCSV beak1 = pixelRepository.findByXposAndYpos(XOfDuckHead + 1, YOfDuckHead + 1);
        PixelFromCSV beak2 = pixelRepository.findByXposAndYpos(XOfDuckHead + 2, YOfDuckHead + 1);
        PixelFromCSV torso1 = pixelRepository.findByXposAndYpos(XOfDuckHead - 2, YOfDuckHead + 2);
        PixelFromCSV torso2 = pixelRepository.findByXposAndYpos(XOfDuckHead - 1, YOfDuckHead + 2);
        PixelFromCSV torso3 = pixelRepository.findByXposAndYpos(XOfDuckHead, YOfDuckHead + 2);
        PixelFromCSV torso4 = pixelRepository.findByXposAndYpos(XOfDuckHead + 1, YOfDuckHead + 2);
        PixelFromCSV papatte1 = pixelRepository.findByXposAndYpos(XOfDuckHead - 1, YOfDuckHead + 3);
        PixelFromCSV papatte2 = pixelRepository.findByXposAndYpos(XOfDuckHead, YOfDuckHead + 3);

        head1.setColor("#FFEB3B");
        head2.setColor("#FFEB3B");
        head3.setColor("#FFEB3B");
        torso1.setColor("#FFEB3B");
        torso2.setColor("#FFEB3B");
        torso3.setColor("#FFEB3B");
        torso4.setColor("#FFEB3B");
        papatte1.setColor("#FFEB3B");
        papatte2.setColor("#FFEB3B");

        beak1.setColor("#FF9800");
        beak2.setColor("#FF9800");

        return Stream.of(head1, head2, head3, beak1, beak2, torso1, torso2, torso3, torso4, papatte1, papatte2).collect(Collectors.toList());
    }

    //TODO finish and test get list of PixelFromCSV from remote open ES on 9200
    private List<PixelFromCSV> getPixelToChangeToMakeADuckFromRemoteDatabase(int xOfDuckHead, int yOfDuckHead, String url) {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(url).build();
        RestHighLevelClient highLevelClient = RestClients.create(clientConfiguration).rest();

        ElasticsearchRestTemplate elasticsearchRestTemplate = new ElasticsearchRestTemplate(highLevelClient);

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(constantScoreQuery(boolQuery()
                        .filter(termQuery("x", xOfDuckHead))
                        .filter(termQuery("y", yOfDuckHead))))
                .withPageable(PageRequest.of(0, 1))
                .build();

        PixelFromCSV head1 = elasticsearchRestTemplate.searchOne(build, PixelFromCSV.class).getContent();

        PixelFromCSV head2 = pixelRepository.findByXposAndYpos(xOfDuckHead + 1, yOfDuckHead);
        PixelFromCSV head3 = pixelRepository.findByXposAndYpos(xOfDuckHead, yOfDuckHead + 1);
        PixelFromCSV beak1 = pixelRepository.findByXposAndYpos(xOfDuckHead + 1, yOfDuckHead + 1);
        PixelFromCSV beak2 = pixelRepository.findByXposAndYpos(xOfDuckHead + 2, yOfDuckHead + 1);
        PixelFromCSV torso1 = pixelRepository.findByXposAndYpos(xOfDuckHead - 2, yOfDuckHead + 2);
        PixelFromCSV torso2 = pixelRepository.findByXposAndYpos(xOfDuckHead - 1, yOfDuckHead + 2);
        PixelFromCSV torso3 = pixelRepository.findByXposAndYpos(xOfDuckHead, yOfDuckHead + 2);
        PixelFromCSV torso4 = pixelRepository.findByXposAndYpos(xOfDuckHead + 1, yOfDuckHead + 2);
        PixelFromCSV papatte1 = pixelRepository.findByXposAndYpos(xOfDuckHead - 1, yOfDuckHead + 3);
        PixelFromCSV papatte2 = pixelRepository.findByXposAndYpos(xOfDuckHead, yOfDuckHead + 3);

        head1.setColor("#FFEB3B");
        head2.setColor("#FFEB3B");
        head3.setColor("#FFEB3B");
        torso1.setColor("#FFEB3B");
        torso2.setColor("#FFEB3B");
        torso3.setColor("#FFEB3B");
        torso4.setColor("#FFEB3B");
        papatte1.setColor("#FFEB3B");
        papatte2.setColor("#FFEB3B");

        beak1.setColor("#FF9800");
        beak2.setColor("#FF9800");

        return Stream.of(head1, head2, head3, beak1, beak2, torso1, torso2, torso3, torso4, papatte1, papatte2).collect(Collectors.toList());
    }



}
