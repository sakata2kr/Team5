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
public class MyOrderViewHandler {


    @Autowired
    private MyOrderRepository myOrderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                // view 객체 생성
                MyOrder myOrder = new MyOrder();
                // view 객체에 이벤트의 Value 를 set 함
                myOrder.setOrderid(ordered.getId());
                myOrder.setQty(ordered.getQty());
                myOrder.setFoodcatlogid(ordered.getFoodcaltalogid());
                myOrder.setCustomerid(ordered.getCustomerid());
                // view 레파지 토리에 save
                myOrderRepository.save(myOrder);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenShipped_then_UPDATE_1(@Payload Shipped shipped) {
        try {
            if (shipped.isMe()) {
                // view 객체 조회
                List<MyOrder> myOrderList = myOrderRepository.findByOrderid(shipped.getOrderId());
                for(MyOrder myOrder : myOrderList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myOrder.setDeliveryid(shipped.getId());
                    myOrder.setStatus(shipped.getStatus());
                    // view 레파지 토리에 save
                    myOrderRepository.save(myOrder);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_UPDATE_1(@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                // view 객체 조회
                List<MyOrder> myOrderList = myOrderRepository.findByOrderid(ordered.getId());
                for(MyOrder myOrder : myOrderList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myOrder.setDeliveryid(ordered.getId());
                    myOrder.setStatus(ordered.getStatus());
                    // view 레파지 토리에 save
                    myOrderRepository.save(myOrder);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_2(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (deliveryCanceled.isMe()) {
                // view 객체 조회
                List<MyOrder> myOrderList = myOrderRepository.findByOrderid(deliveryCanceled.getOrderId());
                for(MyOrder myOrder : myOrderList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    myOrder.setStatus(deliveryCanceled.getStatus());
                    // view 레파지 토리에 save
                    myOrderRepository.save(myOrder);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}