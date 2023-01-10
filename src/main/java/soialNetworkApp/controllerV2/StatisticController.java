//package soialNetworkApp.controllerV2;
//
//import io.swagger.annotations.ApiOperation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import soialNetworkApp.api.response.ErrorRs;
//import soialNetworkApp.api.response.RegionStatisticRs;
//import soialNetworkApp.errors.EmptyFieldException;
//import soialNetworkApp.service.StatisticService;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v2/statistics")
//@RequiredArgsConstructor
//@Tag(name = "statistic-controller", description = "Get statistics by social network")
//public class StatisticController {
//
//    private final StatisticService statisticService;
//
//    @GetMapping("/user")
//    @ApiOperation(value = "get the number of all users")
//    public Long getCountUsers() {
//        return statisticService.getCountUsers();
//    }
//
//    @GetMapping("/user/country")
//    @ApiOperation(value = "get the number of all users by country name")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountUsersByCountry(String country) throws EmptyFieldException {
//        return statisticService.getCountUsersByCountry(country);
//    }
//
//    @GetMapping("/user/city")
//    @ApiOperation(value = "get the number of all users by city name")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountUsersByCity(String city) throws EmptyFieldException {
//        return statisticService.getCountUsersByCity(city);
//    }
//
//    @GetMapping("/city")
//    @ApiOperation(value = "get the number of all cities")
//    public Long getCountCities() {
//        return statisticService.getCountCities();
//    }
//
//    @GetMapping("/city/all")
//    @ApiOperation(value = "get cities with number of users")
//    public List<RegionStatisticRs> getCitiesWithCountUsers() {
//        return statisticService.getCitiesWithCountUsers();
//    }
//
//    @GetMapping("/country")
//    @ApiOperation(value = "get the number of all countries")
//    public Long getCountCountries() {
//        return statisticService.getCountCountries();
//    }
//
//    @GetMapping("/country/all")
//    @ApiOperation(value = "get countries with number of all users")
//    public List<RegionStatisticRs> getCountriesWithCountUsers() {
//        return statisticService.getCountriesWithCountUsers();
//    }
//
//    @GetMapping("/dialog")
//    @ApiOperation(value = "get the number of all dialogs")
//    public Long getCountDialogs() {
//        return statisticService.getCountDialogs();
//    }
//
//    @GetMapping("/dialog/user")
//    @ApiOperation(value = "get the number of dialogs by user id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountDialogsByUserId(Long userId) throws EmptyFieldException {
//        return statisticService.getCountDialogsByUserId(userId);
//    }
//
//    @GetMapping("/like")
//    @ApiOperation(value = "get the number of all likes")
//    public Long getCountLikes() {
//        return statisticService.getCountLikes();
//    }
//
//    @GetMapping("/like/entity")
//    @ApiOperation(value = "get the number of likes by post or comment id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountLikesByEntityId(Long entityId) throws EmptyFieldException {
//        return statisticService.getCountLikesByEntityId(entityId);
//    }
//
//    @GetMapping("/message")
//    @ApiOperation(value = "get the number of all messages")
//    public Long getCountMessages() {
//        return statisticService.getCountMessages();
//    }
//
//    @GetMapping("/message/dialog")
//    @ApiOperation(value = "get the number of all messages by dialog id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountMessagesByDialogId(Long dialogId) throws EmptyFieldException {
//        return statisticService.getCountMessagesByDialogId(dialogId);
//    }
//
//    @GetMapping("/message/all")
//    @ApiOperation(value = "get the number of messages by id's of two persons. This method return map where key is description who author, and who recipient." +
//            " And value is number of message")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Map<String, Long> getCountMessagesByTwoUsers(Long firstUserId, Long secondUserId) throws EmptyFieldException {
//        return statisticService.getCountMessagesByTwoUsers(firstUserId, secondUserId);
//    }
//
//    @GetMapping("/post")
//    @ApiOperation(value = "get the number of all posts")
//    public Long getCountPosts() {
//        return statisticService.getCountPosts();
//    }
//
//    @GetMapping("/post/user")
//    @ApiOperation(value = "get the number of post by user id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountPostsByUserId(Long userId) throws EmptyFieldException {
//        return statisticService.getCountPostsByUserId(userId);
//    }
//
//    @GetMapping("/comment/post")
//    @ApiOperation(value = "get the number of comments by post id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountCommentsByPostId(Long postId) throws EmptyFieldException {
//        return statisticService.getCountCommentsByPostId(postId);
//    }
//
//    @GetMapping("/tag")
//    @ApiOperation(value = "get the number of all tags")
//    public Long getCountTags() {
//        return statisticService.getCountTags();
//    }
//
//    @GetMapping("/tag/post")
//    @ApiOperation(value = "get the number of tags by post id")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
//                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))})
//    })
//    public Long getCountTagsByPostId(Long postId) throws EmptyFieldException {
//        return statisticService.getCountTagsByPostId(postId);
//    }
//}
