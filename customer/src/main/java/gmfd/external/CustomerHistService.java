
package gmfd.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="customerhist", url="http://customerhist:8080")
public interface CustomerHistService {

    @RequestMapping(method= RequestMethod.POST, path="/customerHists")
    public void checkValid(@RequestBody CustomerHist customerHist);

}
