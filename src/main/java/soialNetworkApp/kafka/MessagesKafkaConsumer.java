package soialNetworkApp.kafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.kafka.dto.NotificationKafka;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.repository.MessagesRepository;

@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    private final MessagesRepository messagesRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "messages", groupId = "myGroup")
    public void consume(ConsumerRecord<String, String> consumerRecord){
        LOGGER.info(String.format("Json message received -> %s", consumerRecord));
        MessageKafka messageKafka;
        try {
            messageKafka = objectMapper.readValue(consumerRecord.value(), MessageKafka.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Message message = new Message();
        message.setTime(messageKafka.getTime());
        message.setMessageText(messageKafka.getMessageText());
        message.setReadStatus(messageKafka.getReadStatus());
        message.setIsDeleted(messageKafka.getIsDeleted());
        message.setAuthor(messageKafka.getAuthor());
        message.setRecipient(messageKafka.getRecipient());
        message.setDialog(messageKafka.getDialog());
        messagesRepository.save(message);
    }
}
