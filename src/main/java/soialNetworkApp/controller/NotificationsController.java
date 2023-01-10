package soialNetworkApp.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import soialNetworkApp.aop.annotations.UpdateOnlineTime;
import soialNetworkApp.api.response.CommonRs;
import soialNetworkApp.api.response.ErrorRs;
import soialNetworkApp.api.response.NotificationRs;
import soialNetworkApp.service.NotificationsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "notifications-controller", description = "Get, read notifications")
public class NotificationsController {

    private final NotificationsService notificationsService;

    @UpdateOnlineTime
    @GetMapping("/notifications")
    @ApiOperation(value = "get all notifications for user")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<List<NotificationRs>> getNotifications(
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.noteSize}") int itemPerPage) throws Exception {

        return notificationsService.getAllNotificationsByPerson(offset, itemPerPage);
    }

    @UpdateOnlineTime
    @PutMapping("/notifications")
    @ApiOperation(value = "read notification")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonRs<List<NotificationRs>> markAsReadNotification(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "false") boolean all) throws Exception {

        return notificationsService.markNotificationStatusAsRead(id, all);
    }
}
