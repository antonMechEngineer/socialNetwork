package soialNetworkApp.mappers;

import org.mapstruct.*;
import soialNetworkApp.api.request.MessageWsRq;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.model.entities.Message;
import soialNetworkApp.model.enums.ReadStatusTypes;
import soialNetworkApp.repository.DialogsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.service.DialogsService;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring", uses={DialogsRepository.class, PersonsRepository.class, DialogsService.class}, imports = {ZonedDateTime.class, ReadStatusTypes.class})
public interface MessageMapper {


    @Mapping(target = "dialog", source = "messageWsRq.dialogId", expression = "java(dialogsRepository.findById())")
    @Mapping(target = "author", source = "messageWsRq.authorId", expression = "java(personsRepository.findById())")
    @Mapping(target = "recipient", source = "messageWsRq.dialogId, messageWsRq.authorId", qualifiedByName = "getRecipientFromDialog")
    Message toMessageFromWs(MessageWsRq messageWsRq);

    default ZonedDateTime getTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
    }

    default ReadStatusTypes getReadStatus(String readStatus) {
        return ReadStatusTypes.valueOf(readStatus);
    }

}
