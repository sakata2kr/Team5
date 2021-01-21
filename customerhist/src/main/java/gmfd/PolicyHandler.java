package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){
        logger.info(eventString + " onStringEventListener !!");
    }

    //Bean 간 연결
    @Autowired
    gmfd.CustomerHistRepository customerHistRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever_updated(@Payload Updated updated){

        if(updated.isMe()){
            logger.info("##### listener customer addModHist : " + updated.toJson());

            gmfd.CustomerHist customerHist = new gmfd.CustomerHist();

            customerHist.setCustomerid(updated.getId());
            customerHist.setName(updated.getName());
            customerHist.setAddress(updated.getAddress());
            customerHist.setAge(updated.getAge());
            customerHist.setPhone(updated.getPhone());
            customerHist.setStatus("MOD");

            customerHistRepository.save(customerHist);

        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever_updated(@Payload Withdrawed withdrawed){

        if(withdrawed.isMe()){
            logger.info("##### listener customer addDelHist : " + withdrawed.toJson());

            gmfd.CustomerHist customerHist = new gmfd.CustomerHist();
            customerHist.setCustomerid(withdrawed.getId());
            customerHist.setName(withdrawed.getName());
            customerHist.setAddress(withdrawed.getAddress());
            customerHist.setAge(withdrawed.getAge());
            customerHist.setPhone(withdrawed.getPhone());
            customerHist.setStatus("DEL");

            customerHistRepository.save(customerHist);

        }
    }
}
