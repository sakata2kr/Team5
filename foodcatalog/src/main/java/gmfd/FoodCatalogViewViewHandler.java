package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FoodCatalogViewViewHandler {


    @Autowired
    private FoodCatalogViewRepository foodCatalogViewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCatalogRegistered_then_CREATE_1 (@Payload CatalogRegistered catalogRegistered) {
        try {
            if (catalogRegistered.isMe()) {
                // view 객체 생성
                FoodCatalogView foodCatalogView = new FoodCatalogView();
                // view 객체에 이벤트의 Value 를 set 함
                foodCatalogView.setFoodcatalogid(catalogRegistered.getId());
                foodCatalogView.setStock(catalogRegistered.getStock());
                foodCatalogView.setName(catalogRegistered.getName());
                foodCatalogView.setPrice(catalogRegistered.getPrice());
                // view 레파지 토리에 save
                foodCatalogViewRepository.save(foodCatalogView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenCatalogUpdated_then_UPDATE_1(@Payload CatalogUpdated catalogUpdated) {
        try {
            if (catalogUpdated.isMe()) {
                // view 객체 조회
                List<FoodCatalogView> foodCatalogViewList = foodCatalogViewRepository.findByfoodcatalogid(catalogUpdated.getId());
                for(FoodCatalogView foodCatalogView : foodCatalogViewList) {
                    //FoodCatalogView foodCatalogView = foodCatalogViewOptional.get();
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    foodCatalogView.setStock(catalogUpdated.getStock());
                    foodCatalogView.setName(catalogUpdated.getName());
                    foodCatalogView.setPrice(catalogUpdated.getPrice());
                    // view 레파지 토리에 save
                    foodCatalogViewRepository.save(foodCatalogView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCatalogDeleted_then_DELETE_1(@Payload CatalogDeleted catalogDeleted) {
        try {
            if (catalogDeleted.isMe()) {
                // view 레파지 토리에 삭제 쿼리
                foodCatalogViewRepository.deleteById(catalogDeleted.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}