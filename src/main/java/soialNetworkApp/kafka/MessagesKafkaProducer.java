package soialNetworkApp.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.model.entities.Message;

@Slf4j
@Service
public class MessagesKafkaProducer {
    private KafkaTemplate<String, MessageKafka> kafkaTemplate;
    public MessagesKafkaProducer(KafkaTemplate<String, MessageKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage (Message messageEntity){
        log.info(String.format("Sent -> %s", messageEntity.toString()));
        MessageKafka messageKafka = new MessageKafka(
                messageEntity.getTime(),
                messageEntity.getMessageText(),
                messageEntity.getReadStatus(),
                messageEntity.getIsDeleted(),
                messageEntity.getAuthor().getId(),
                messageEntity.getRecipient().getId(),
                messageEntity.getDialog().getId());
        kafkaTemplate.send("messages", messageKafka);
    }
}
