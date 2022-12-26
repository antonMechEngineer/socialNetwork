//package soialNetworkApp.kafka;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//@Service
//
//public class MessagesKafkaProducer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesKafkaProducer.class);
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public MessagesKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage (Object data){
//        LOGGER.info(String.format("Message sent -> %s", data.toString()));
//        Message<Object> message = MessageBuilder.withPayload(data).
//                setHeader(KafkaHeaders.TOPIC, "messages").build();
//        kafkaTemplate.send(message);
//    }
//}
