package soialNetworkApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import soialNetworkApp.api.response.DialogRs;
import soialNetworkApp.api.response.MessageRs;
import soialNetworkApp.model.entities.Dialog;
import soialNetworkApp.service.DialogsService;
import soialNetworkApp.service.MessageService;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", uses = {MessageService.class}, imports = {ZonedDateTime.class})
public interface DialogMapper {

    @Mapping(target = "id", source = "dialog.id")
    @Mapping(target = "lastMessage", source = "messageRs")
    @Mapping(target = "unreadCount", source = "dialog", qualifiedByName = "getUnreadMessagesCountForDialog")
    DialogRs toDialogRs(MessageRs messageRs, Dialog dialog);
}
