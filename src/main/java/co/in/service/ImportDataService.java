package co.in.service;

import co.in.entity.PixelFromCSV;
import co.in.repository.PixelFromCSVRepository;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@ShellComponent
public class ImportDataService {

    private final PixelFromCSVRepository pixelRepository;

    @Autowired
    public ImportDataService(PixelFromCSVRepository pixelRepository) {
        this.pixelRepository = pixelRepository;
    }

    @ShellMethod("Importe moi une db")
    public String coimport() throws IOException {

        ClassPathResource resource = new ClassPathResource("dby_flag.csv");
        InputStream inputStream = resource.getInputStream();
        List<PixelFromCSV> pixels = new CsvToBeanBuilder(new CSVReader(new InputStreamReader(inputStream)))
                .withType(PixelFromCSV.class)
                .build()
                .parse();

        pixelRepository.saveAll(pixels);
        return "tout bon";
    }


}
