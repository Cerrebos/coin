package co.in.repository;

import co.in.entity.Pixel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PixelRepository extends CrudRepository<Pixel, String> {

    Pixel findByXAndY(int x, int y);

}
