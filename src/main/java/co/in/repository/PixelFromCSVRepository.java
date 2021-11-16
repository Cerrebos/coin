package co.in.repository;

import co.in.entity.PixelFromCSV;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixelFromCSVRepository extends CrudRepository<PixelFromCSV, String> {

    PixelFromCSV findByXposAndYpos(int x, int y);

}
