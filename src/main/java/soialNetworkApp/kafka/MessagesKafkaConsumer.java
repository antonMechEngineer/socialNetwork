package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    private final MessagesRepository messagesRepository;
    private final DialogsRepository dialogsRepository;
    private final DialogMapper dialogMapper;


    @KafkaListener(topics = "messages", autoStartup = "${listen.auto.start:true}")
    public void consume(MessageKafka messageKafka) {
        log.info(String.format("Json received -> %s", messageKafka.toString()));
        Message message = dialogMapper.toMessageFromKafka(messageKafka);
        messagesRepository.save(message);
        Dialog dialog = dialogsRepository.findById(messageKafka.getDialogId()).orElseThrow();
        dialog.setLastMessage(message);
        dialogsRepository.save(dialog);
    }
}
