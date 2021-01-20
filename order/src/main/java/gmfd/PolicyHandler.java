package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.BeanUtils;
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
    OrderRepository orderRepository;
    //
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_UpdateStatus(@Payload Shipped shipped){

        if(shipped.isMe()){
            System.out.println("##### listener UpdateStatus : " + shipped.toJson());

            Optional<Order> orderOptional = orderRepository.findById(shipped.getOrderId());

            Order order = orderOptional.get();
            //order.setId(shipped.getOrderId());
            order.setStatus(shipped.getStatus());

            orderRepository.save(order);

        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void onCatalogOutofStock(@Payload CatalogOutofstock catalogOutofstock) {
        try {
            if (catalogOutofstock.isMe()) {
                System.out.println("##### listener : " + catalogOutofstock.toJson());
                Optional<Order> orderOptional = orderRepository.findById(catalogOutofstock.getOrderId());
                Order order = orderOptional.get();
                order.setStatus("OrderCancelled");

                //OrderCancelled orderCancelled = new OrderCancelled();

                OrderCancelled orderCancelled = new OrderCancelled();
                BeanUtils.copyProperties(order, orderCancelled);
                //orderCancelled.publishAfterCommit();

                orderCancelled.setId(order.getId());
                orderCancelled.setCustomerid(order.getCustomerid());
                orderCancelled.setFoodcaltalogid(order.getFoodcaltalogid());
                orderCancelled.setQty(order.getQty());
                orderCancelled.setStatus("Order Canceled");
                orderCancelled.publish();

                orderRepository.save(order);

                //Following code causes dependency to external APIs
                // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.


                gmfd.external.Cancellation cancellation = new gmfd.external.Cancellation();
                // mappings goes here
                cancellation.setOrderId(order.getId());
                cancellation.setStatus("Cancelled");




                OrderApplication.applicationContext.getBean(gmfd.external.CancellationService.class)
                        .cancelShip(cancellation);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
