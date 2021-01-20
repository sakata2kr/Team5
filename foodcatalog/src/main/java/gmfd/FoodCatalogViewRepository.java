package gmfd;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodCatalogViewRepository extends CrudRepository<FoodCatalogView, Long> {

    //findby 할때 View에서 생성한 변수와 동일한 변수명을 사용해야 함
    List<FoodCatalogView> findByfoodcatalogid(Long foodcatalogid);
}