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
    FoodCatalogRepository foodCatalogRepository;
    //
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_UpdateStock(@Payload Ordered ordered){

        if(ordered.isMe()){
            System.out.println("##### listener UpdateStock : " + ordered.toJson());

            Optional<FoodCatalog> optionalFoodCatalog = foodCatalogRepository.findById(ordered.getFoodcaltalogid());

            FoodCatalog foodCatalog = optionalFoodCatalog.get();

            foodCatalog.setStock(foodCatalog.getStock() - ordered.getQty());

            if( foodCatalog.getStock() < 0 ){
                System.out.println("productOutOfStock 이벤트 발생");
                CatalogOutofstock catalogOutofstock = new CatalogOutofstock();
                catalogOutofstock.setProductId(ordered.getFoodcaltalogid());
                catalogOutofstock.setOrderId(ordered.getId());
                catalogOutofstock.publish();

            }else{
                foodCatalogRepository.save(foodCatalog);
            }



        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCancelled_UpdateStock(@Payload OrderCancelled orderCancelled){

        if(orderCancelled.isMe()){
            System.out.println("##### listener UpdateStock : " + orderCancelled.toJson());

            Optional<FoodCatalog> optionalFoodCatalog = foodCatalogRepository.findById(orderCancelled.getFoodcaltalogid());

            FoodCatalog foodCatalog = optionalFoodCatalog.get();


                foodCatalog.setStock(foodCatalog.getStock() + orderCancelled.getQty());


            foodCatalogRepository.save(foodCatalog);

        }
    }

}
