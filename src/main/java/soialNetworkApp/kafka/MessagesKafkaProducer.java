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

    public void sendMessage(MessageWs messageWsRq) {
        log.info(messageWsRq.getAuthorId().toString());
        MessageKafka messageKafka = dialogMapper.toMessageKafkaFromMessageWs(messageWsRq);
        kafkaTemplate.send("messages", messageKafka);
    }
}
