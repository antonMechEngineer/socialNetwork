package soialNetworkApp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import soialNetworkApp.kafka.dto.MessageKafka;
import soialNetworkApp.mappers.DialogMapper;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.MessagesRepository;
import soialNetworkApp.service.NotificationsService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesKafkaConsumer {

    @Value("${socialNetwork.timezone}")
    private String timezone;

    private final MessagesRepository messagesRepository;
    private final DialogsRepository dialogsRepository;
    private final DialogMapper dialogMapper;
    private final NotificationsService notificationsService;
    private final NotificationsKafkaProducer notificationsKafkaProducer;


    @KafkaListener(topics = "messages", autoStartup = "${listen.auto.start:true}")
    public void consume(MessageKafka messageKafka) {
        log.info(String.format("Json received -> %s", messageKafka.toString()));
        Message message = dialogMapper.toMessageFromKafka(messageKafka);
        messagesRepository.save(message);
        Dialog dialog = dialogsRepository.findById(messageKafka.getDialogId()).orElseThrow();
        dialog.setLastMessage(message);
        dialogsRepository.save(dialog);
        if (message.getRecipient().getPersonSettings().getMessageNotification() &&
                LocalDateTime.now(ZoneId.of(timezone)).minus(1, ChronoUnit.MINUTES)
                        .isAfter(message.getRecipient().getLastOnlineTime())) {
            notificationsKafkaProducer.sendMessage(message, message.getRecipient());
            notificationsService.sendNotificationToTelegramBot(message, message.getRecipient());
        }
    }
}
