package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }


    //Bean 간 연결
    @Autowired
    PayRepository payRepository;
    //
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCanceled_Cancelled(@Payload DeliveryCanceled deliveryCanceled){

        if(deliveryCanceled.isMe()){
            System.out.println("##### listener Cancelled : " + deliveryCanceled.toJson());

            Optional<Pay> optionalPay = payRepository.findById(deliveryCanceled.getOrderId());

            Pay pay = optionalPay.get();

            //pay.setOrderid(deliveryCanceled.getOrderId());
            pay.setStatus("Order Canceled-payment");

            payRepository.save(pay);
            //

            Cancelled cancelled = new Cancelled();
            cancelled.setAmout(pay.getAmout());
            cancelled.setId(pay.getId());
            cancelled.setOrderid(pay.getOrderid());
            cancelled.setStatus(pay.getStatus());
            cancelled.publish();


        }
    }

}
