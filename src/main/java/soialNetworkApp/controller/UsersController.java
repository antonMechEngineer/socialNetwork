package soialNetworkApp.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import soialNetworkApp.aop.annotations.UpdateOnlineTime;
import soialNetworkApp.api.request.FindPersonRq;
import soialNetworkApp.api.request.PostRequest;
import soialNetworkApp.api.request.UserRq;
import soialNetworkApp.api.response.*;
import soialNetworkApp.errors.EmptyFieldException;
import soialNetworkApp.errors.PersonNotFoundException;
import soialNetworkApp.service.PersonsService;
import soialNetworkApp.service.PostsService;
import soialNetworkApp.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@UpdateOnlineTime
@Tag(name = "users-controller",
        description = "Get user, get users post, create post. Get, update, delete, recover personal info. User search")
public class UsersController {

    private final PostsService postsService;
    private final PersonsService personsService;
    private final UsersService usersService;

    @UpdateOnlineTime
    @GetMapping("/{id}")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<PersonResponse> getUserById(@PathVariable long id) {
        return personsService.getPersonDataById(id);
    }

    @UpdateOnlineTime
    @GetMapping("/{id}/wall")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "get all post by author id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<PostResponse>> getUsersPosts(
            @PathVariable long id,
            @RequestParam(name = "offset", required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(name = "itemPerPage", required = false, defaultValue = "${socialNetwork.default.size}") int size) {

        return postsService.getAllPostsByAuthor(offset, size, personsService.getPersonById(id));
    }

    @UpdateOnlineTime
    @PostMapping("/{id}/wall")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "create new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<PostResponse> createPost(
            @PathVariable(name = "id") Long personId,
            @RequestParam(name = "publish_date", required = false) Long publishingDate,
            @RequestBody PostRequest postRequest) throws PersonNotFoundException {

        return postsService.createPost(postRequest, personId, publishingDate);
    }

    @UpdateOnlineTime
    @GetMapping("/me")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "get information about me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<PersonResponse> getMyData() {
        return personsService.getMyData();
    }

    @UpdateOnlineTime
    @PutMapping("/me")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "update information about me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    ResponseEntity<UserRs> updateMyData(@RequestBody UserRq userRq) throws Exception {
        return ResponseEntity
                .ok(usersService.editProfile(userRq));
    }

    @DeleteMapping("/me")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "delte information about me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    ResponseEntity<ResponseRsComplexRs> deleteMyData() {
        return ResponseEntity
                .ok(usersService.deleteProfile());
    }
    @PostMapping("/me/recover")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "recover information about me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    ResponseEntity<ResponseRsComplexRs> recoverMyData() {
        return ResponseEntity
                .ok(usersService.recoverProfile());
    }

    @UpdateOnlineTime
    @GetMapping("/search")
    @ApiImplicitParam(name = "authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "JWT token")
    @ApiOperation(value = "search post by query")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "\"Name of error\"",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRs.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "forbidden")
    })
    public CommonResponse<List<PersonResponse>> findPersons(
            FindPersonRq personRq,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.page}") int offset,
            @RequestParam(required = false, defaultValue = "${socialNetwork.default.size}") int perPage) throws SQLException, EmptyFieldException {
        return usersService.findPersons(personRq, offset, perPage);
    }
}
