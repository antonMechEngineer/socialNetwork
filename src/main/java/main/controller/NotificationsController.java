package main.controller;

import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
import main.api.response.CommonResponse;
import main.api.response.NotificationResponse;
import main.service.NotificationsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationsService notificationsService;

    @UpdateOnlineTime
    @GetMapping("/notifications")
    public CommonResponse<List<NotificationResponse>> getNotifications(
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.noteSize}") int itemPerPage) {

        return notificationsService.getAllNotificationsByPerson(offset, itemPerPage);
    }

    @UpdateOnlineTime
    @PutMapping("/notifications")
    public CommonResponse<List<NotificationResponse>> markAsReadNotification(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "false") boolean all) {

        return notificationsService.markNotificationStatusAsRead(id, all);
    }
}
