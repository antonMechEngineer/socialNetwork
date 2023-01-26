package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.websocket.MessageWs;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.mappers.DialogMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaProducer {

    private final KafkaTemplate<String, MessageKafka> kafkaTemplate;
    private final DialogMapper dialogMapper;

    public void sendMessage(MessageWs messageWsRq){
        MessageKafka messageKafka = dialogMapper.toMessageKafkaFromMessageWs(messageWsRq);
        log.info(messageKafka.toString());
        sendMessageKafka(messageKafka);
    }

    private void sendMessageKafka(MessageKafka messageKafka){
        log.info(String.format("Sent -> %s", messageKafka.toString()));
        kafkaTemplate.send("messages", messageKafka);
    }
}
