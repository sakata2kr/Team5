
package gmfd.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="foodCatalog", url="${api.foodCatalog.url}")
public interface FoodCatalogService {

    @RequestMapping(method= RequestMethod.GET, path="/foodCatalogs")
    public void queryCatalog(@RequestBody FoodCatalog foodCatalog);

}
