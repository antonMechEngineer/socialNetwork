package soialNetworkApp.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.model.entities.Message;

@Service

public class MessagesKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaProducer.class);
    private KafkaTemplate<String, MessageKafka> kafkaTemplate;

    public MessagesKafkaProducer(KafkaTemplate<String, MessageKafka> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage (Message messageEntity){
        LOGGER.info(String.format("Message sent -> %s", messageEntity.toString()));
        MessageKafka messageKafka = new MessageKafka(
                messageEntity.getTime(),
                messageEntity.getMessageText(),
                messageEntity.getReadStatus(),
                messageEntity.getIsDeleted(),
                messageEntity.getAuthor().getId(),
                messageEntity.getRecipient().getId(),
                messageEntity.getDialog().getId());

        org.springframework.messaging.Message<MessageKafka> message = MessageBuilder.withPayload(messageKafka).
                setHeader(KafkaHeaders.TOPIC, "messages").build();
        kafkaTemplate.send(message);
    }
}
