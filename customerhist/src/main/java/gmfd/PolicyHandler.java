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
    public void whenever_updated(@Payload AddModHist addModHist){

        if(addModHist.isMe()){
            logger.info("##### listener customer addModHist : " + addModHist.toJson());

            gmfd.CustomerHist customerHist = new gmfd.CustomerHist();

            customerHist.setCustomerid(addModHist.getId());
            customerHist.setName(addModHist.getName());
            customerHist.setAddress(addModHist.getAddress());
            customerHist.setAge(addModHist.getAge());
            customerHist.setPhone(addModHist.getPhone());
            customerHist.setStatus("MOD");

            customerHistRepository.save(customerHist);

        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever_updated(@Payload AddDelHist addDelHist){

        if(addDelHist.isMe()){
            logger.info("##### listener customer addDelHist : " + addDelHist.toJson());

            gmfd.CustomerHist customerHist = new gmfd.CustomerHist();
            customerHist.setCustomerid(addDelHist.getId());
            customerHist.setName(addDelHist.getName());
            customerHist.setAddress(addDelHist.getAddress());
            customerHist.setAge(addDelHist.getAge());
            customerHist.setPhone(addDelHist.getPhone());
            customerHist.setStatus("DEL");

            customerHistRepository.save(customerHist);

        }
    }
}
