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
    DeliveryRepository deliveryRepository;
    //PayRepository payRepository;
    //
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_Ship(@Payload Paid paid){
        /*
        if(paid.isMe()){
            System.out.println("##### listener Ship : " + paid.toJson());

            paid.setStatus("Paid");

            Delivery delivery = new Delivery();

            delivery.setOrderId(paid.getOrderid());
            delivery.setStatus("Delivery Start");



            deliveryRepository.save(delivery);


        }
        */
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_UpdateStatus(@Payload Paid paid){

        if(paid.isMe()){
            System.out.println("##### listener Ship : " + paid.toJson());

            Delivery delivery = new Delivery();

            delivery.setOrderId(paid.getOrderid());
            delivery.setStatus(paid.getStatus());


            deliveryRepository.save(delivery);


        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void onOrderCancelled(@Payload OrderCancelled orderCancelled){

        if(orderCancelled.isMe()){
            System.out.println("##### listener Cancelled : " + orderCancelled.toJson());

            Optional<Delivery> optionalDelivery = deliveryRepository.findById(orderCancelled.getId());

            Delivery delivery = optionalDelivery.get();

            //pay.setOrderid(deliveryCanceled.getOrderId());
            delivery.setStatus("Order Canceled-delivery");

            deliveryRepository.save(delivery);
            //

            /*Cancelled cancelled = new Cancelled();
            cancelled.setAmout(pay.getAmout());
            cancelled.setId(pay.getId());
            cancelled.setOrderid(pay.getOrderid());
            cancelled.setStatus(pay.getStatus());
            cancelled.publish();*/


        }
    }


}
