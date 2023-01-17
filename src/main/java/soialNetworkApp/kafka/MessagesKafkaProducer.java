package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.kafka.mappers.MessageKafkaMapper;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.repository.DialogsRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaProducer {
    private final DialogsRepository dialogsRepository;
    private final KafkaTemplate<String, MessageKafka> kafkaTemplate;
    private final MessageKafkaMapper messageKafkaMapper;

    public void sendMessage (MessageWsRq messageWsRq){
        log.info(String.format("Sent -> %s", messageWsRq.toString()));
        MessageKafka messageKafka = messageKafkaMapper.toMessageKafkaFromMessageWs(messageWsRq,
                dialogsRepository.findById(messageWsRq.getDialogId()).orElseThrow());
        kafkaTemplate.send("messages", messageKafka);
    }

    public void sendMessage(Message message){
        log.info(String.format("Sent -> %s", message.toString()));
        MessageKafka messageKafka = messageKafkaMapper.toMessageKafkaFromMessage(message);
        kafkaTemplate.send("messages", messageKafka);
    }
}
