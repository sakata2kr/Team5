package gmfd.external;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="myorder", url="${api.payment.url}")
public interface myOrderService  {

    @RequestMapping(method= RequestMethod.GET, path="/myorders")
    public void myrOder(@RequestBody MyOrder myOrder);

}

