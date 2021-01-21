package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeHistViewHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ChangeHistRepository changeHistRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Registerd registerd) {
        try {
            if (registerd.isMe()) {
                logger.info("##### listener customer registered : " + registerd.toJson());

                ChangeHist changeHist = new ChangeHist();

                changeHist.setCustomerid(registerd.getId());
                changeHist.setName(registerd.getName());
                changeHist.setAddress(registerd.getAddress());
                changeHist.setAge(registerd.getAge());
                changeHist.setPhone(registerd.getPhone());
                changeHist.setStatus("NEW");

                changeHistRepository.save(changeHist);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever_updated(@Payload Updated updated){

        if(updated.isMe()){
            logger.info("##### listener customer addModHist : " + updated.toJson());

            ChangeHist changeHist = new ChangeHist();

            changeHist.setCustomerid(updated.getId());
            changeHist.setName(updated.getName());
            changeHist.setAddress(updated.getAddress());
            changeHist.setAge(updated.getAge());
            changeHist.setPhone(updated.getPhone());
            changeHist.setStatus("MOD");

            changeHistRepository.save(changeHist);

        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenever_updated(@Payload Withdrawed withdrawed){

        if(withdrawed.isMe()){
            logger.info("##### listener customer addDelHist : " + withdrawed.toJson());

            ChangeHist changeHist = new ChangeHist();

            changeHist.setCustomerid(withdrawed.getId());
            changeHist.setName(withdrawed.getName());
            changeHist.setAddress(withdrawed.getAddress());
            changeHist.setAge(withdrawed.getAge());
            changeHist.setPhone(withdrawed.getPhone());
            changeHist.setStatus("DEL");

            changeHistRepository.save(changeHist);

        }
    }

}