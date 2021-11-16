package co.in.service;

import co.in.entity.Pixel;
import co.in.repository.PixelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class ImportDataService {

    public static final String CODATI_URL      = "https://api.codati.ovh/pixels/?index=";
    public static final String FOULOSCOPIE_URL = "https://api-flag.fouloscopie.com/flag";

    private final RestTemplate restTemplate;
    private final PixelRepository pixelRepository;

    @Autowired
    public ImportDataService(RestTemplateBuilder builder, PixelRepository pixelRepository) {
        this.restTemplate = builder.build();
        this.pixelRepository = pixelRepository;
    }

    public void generateEnrichedFlagData() {
        //get all the pixels from fouloscopie (warning : sometimes the server only answer a chunk, don't know why...)
        List<Pixel> pixelsDataFromFouloscopie = Arrays.asList(restTemplate.getForObject(FOULOSCOPIE_URL, Pixel[].class));
        System.out.println("Pixels number retrieved from Fouloscopie : " + pixelsDataFromFouloscopie.size());
        //for each pixel retrieved, we need the "entityId" so that we have x, y , entityId in the same DB, allowing us to draw stuff easily and do the PUT request on
        //fouloscopie which requires the entityId
        pixelsDataFromFouloscopie.stream()
                .map(this::enrichPixelWithCodatiData)
                .forEach(pixelRepository::save);
    }

    private Pixel enrichPixelWithCodatiData(Pixel fouloscopiePixel) {
        if (fouloscopiePixel.getIndexInFlag() > 1) { //first index is 1, we ignore this for simplicity reason
            //TODO There seems to be an error in the matching of indexInFlag at codati, we must solve it and regenerate the DB in order to make this who thing work
            Pixel pixel = restTemplate.getForObject(CODATI_URL + (fouloscopiePixel.getIndexInFlag() - 1), Pixel.class);
            if (pixel != null) {
                pixel.setEntityId(fouloscopiePixel.getEntityId());
                System.out.println("pixel treated for indexInFlag: " + pixel.getIndexInFlag());
            }
            return pixel;
        }
        return fouloscopiePixel;
    }

}
