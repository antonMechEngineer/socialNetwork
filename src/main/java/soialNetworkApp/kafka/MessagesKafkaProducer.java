package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.repository.DialogsRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaProducer {
    private final DialogsRepository dialogsRepository;
    private final KafkaTemplate<String, MessageKafka> kafkaTemplate;
    private final DialogMapper dialogMapper;

    public void sendMessage(MessageWsRq messageWsRq){
        MessageKafka messageKafka = dialogMapper.toMessageKafkaFromMessageWs(messageWsRq,
                dialogsRepository.findById(messageWsRq.getDialogId()).orElseThrow());
        sendMessageKafka(messageKafka);
    }

    public void sendMessage(Message message){
        MessageKafka messageKafka = dialogMapper.toMessageKafkaFromMessage(message);
        sendMessageKafka(messageKafka);
    }

    private void sendMessageKafka(MessageKafka messageKafka){
        log.info(String.format("Sent -> %s", messageKafka.toString()));
        kafkaTemplate.send("messages", messageKafka);
    }
}
