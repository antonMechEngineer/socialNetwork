package main.controller;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/notifications")
    public CommonResponse<List<NotificationResponse>> getNotifications(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int itemPerPage) {

        return notificationsService.getAllNotificationsByPerson(offset, itemPerPage);
    }

    @PutMapping("/notifications")
    public CommonResponse<List<NotificationResponse>> markAsReadNotification(
            @RequestParam(required = false) Long id,
            @RequestParam boolean all) {

        return notificationsService.markNotificationStatusAsRead(id, all);
    }
}
