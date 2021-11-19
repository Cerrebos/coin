package co.in.service;

import co.in.entity.Pixel;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ImportDataService {

    public List<Pixel> getAllPixels() throws IOException {

        ClassPathResource resource = new ClassPathResource("dby_flag.csv");
        InputStream inputStream = resource.getInputStream();
        List<Pixel> pixels = new CsvToBeanBuilder(new CSVReader(new InputStreamReader(inputStream)))
                .withType(Pixel.class)
                .build()
                .parse();

        return pixels;
    }


}
