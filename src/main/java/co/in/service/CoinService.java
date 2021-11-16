package co.in.service;

import co.in.entity.Pixel;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class CoinService {

    public static final String FOULOSCOPIE_URL_PUT  = "https://api-flag.fouloscopie.com/pixel";

    private final PixelRepository pixelRepository;

    @Autowired
    public CoinService(PixelRepository pixelRepository) {
        this.pixelRepository = pixelRepository;
    }

    public void runDuckGeneration(String sessionId, String ngrokUrl, int x, int y) {
        getPixelToChangeToMakeADuck(x, y)
                        .forEach(pixel ->  sendRequest(sessionId, pixel.getEntityId(), pixel.getHexColor()));
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
            Thread.sleep(1000 * 63 * 2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    private List<Pixel> getPixelToChangeToMakeADuck(int XOfDuckHead, int YOfDuckHead) {

        Pixel head1 = pixelRepository.findByXAndY(XOfDuckHead, YOfDuckHead);
        Pixel head2 = pixelRepository.findByXAndY(XOfDuckHead + 1, YOfDuckHead);
        Pixel head3 = pixelRepository.findByXAndY(XOfDuckHead, YOfDuckHead + 1);
        Pixel beak1 = pixelRepository.findByXAndY(XOfDuckHead + 1, YOfDuckHead + 1);
        Pixel beak2 = pixelRepository.findByXAndY(XOfDuckHead + 2, YOfDuckHead + 1);
        Pixel torso1 = pixelRepository.findByXAndY(XOfDuckHead - 2, YOfDuckHead + 2);
        Pixel torso2 = pixelRepository.findByXAndY(XOfDuckHead - 1, YOfDuckHead + 2);
        Pixel torso3 = pixelRepository.findByXAndY(XOfDuckHead, YOfDuckHead + 2);
        Pixel torso4 = pixelRepository.findByXAndY(XOfDuckHead + 1, YOfDuckHead + 2);
        Pixel papatte1 = pixelRepository.findByXAndY(XOfDuckHead - 1, YOfDuckHead + 3);
        Pixel papatte2 = pixelRepository.findByXAndY(XOfDuckHead, YOfDuckHead + 3);

        head1.setHexColor("#FFEB3B");
        head2.setHexColor("#FFEB3B");
        head3.setHexColor("#FFEB3B");
        torso1.setHexColor("#FFEB3B");
        torso2.setHexColor("#FFEB3B");
        torso3.setHexColor("#FFEB3B");
        torso4.setHexColor("#FFEB3B");
        papatte1.setHexColor("#FFEB3B");
        papatte2.setHexColor("#FFEB3B");

        beak1.setHexColor("#FF9800");
        beak2.setHexColor("#FF9800");

        return Stream.of(head1, head2, head3, beak1, beak2, torso1, torso2, torso3, torso4, papatte1, papatte2).collect(Collectors.toList());
    }

    //TODO finish and test get list of Pixel from remote open ES on 9200
    private List<Pixel> getPixelToChangeToMakeADuckFromRemoteDatabase(int xOfDuckHead, int yOfDuckHead, String url) {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo(url).build();
        RestHighLevelClient highLevelClient = RestClients.create(clientConfiguration).rest();

        ElasticsearchRestTemplate elasticsearchRestTemplate = new ElasticsearchRestTemplate(highLevelClient);

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(constantScoreQuery(boolQuery()
                        .filter(termQuery("x", xOfDuckHead))
                        .filter(termQuery("y", yOfDuckHead))))
                .withPageable(PageRequest.of(0, 1))
                .build();

        Pixel head1 = elasticsearchRestTemplate.searchOne(build, Pixel.class).getContent();

        Pixel head2 = pixelRepository.findByXAndY(xOfDuckHead + 1, yOfDuckHead);
        Pixel head3 = pixelRepository.findByXAndY(xOfDuckHead, yOfDuckHead + 1);
        Pixel beak1 = pixelRepository.findByXAndY(xOfDuckHead + 1, yOfDuckHead + 1);
        Pixel beak2 = pixelRepository.findByXAndY(xOfDuckHead + 2, yOfDuckHead + 1);
        Pixel torso1 = pixelRepository.findByXAndY(xOfDuckHead - 2, yOfDuckHead + 2);
        Pixel torso2 = pixelRepository.findByXAndY(xOfDuckHead - 1, yOfDuckHead + 2);
        Pixel torso3 = pixelRepository.findByXAndY(xOfDuckHead, yOfDuckHead + 2);
        Pixel torso4 = pixelRepository.findByXAndY(xOfDuckHead + 1, yOfDuckHead + 2);
        Pixel papatte1 = pixelRepository.findByXAndY(xOfDuckHead - 1, yOfDuckHead + 3);
        Pixel papatte2 = pixelRepository.findByXAndY(xOfDuckHead, yOfDuckHead + 3);

        head1.setHexColor("#FFEB3B");
        head2.setHexColor("#FFEB3B");
        head3.setHexColor("#FFEB3B");
        torso1.setHexColor("#FFEB3B");
        torso2.setHexColor("#FFEB3B");
        torso3.setHexColor("#FFEB3B");
        torso4.setHexColor("#FFEB3B");
        papatte1.setHexColor("#FFEB3B");
        papatte2.setHexColor("#FFEB3B");

        beak1.setHexColor("#FF9800");
        beak2.setHexColor("#FF9800");

        return Stream.of(head1, head2, head3, beak1, beak2, torso1, torso2, torso3, torso4, papatte1, papatte2).collect(Collectors.toList());
    }



}
