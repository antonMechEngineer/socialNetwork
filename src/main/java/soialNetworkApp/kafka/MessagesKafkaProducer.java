//package soialNetworkApp.kafka;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import soialNetworkApp.kafka.dto.MessageKafka;
//import soialNetworkApp.model.entities.Message;
//
//@Service
//
//public class MessagesKafkaProducer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaProducer.class);
//    private KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public MessagesKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage (Message message){
//        LOGGER.info(String.format("Message sent -> %s", message.toString()));
//
//        MessageKafka messageKafka = new MessageKafka(
//               message.getTime(),
//               message.getMessageText(),
//               message.getReadStatus(),
//               message.getIsDeleted(),
//               message.getAuthor(),
//               message.getRecipient(),
//               message.getDialog());
//        String messageText = "";
//        try {
//            messageText = objectMapper.writeValueAsString(messageText);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        kafkaTemplate.send("messages", messageText);
//    }
//}
