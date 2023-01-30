package soialNetworkApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import soialNetworkApp.aop.annotations.UpdateOnlineTime;
import soialNetworkApp.api.response.*;
import soialNetworkApp.errors.FileException;
import soialNetworkApp.service.UsersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "storage-controller", description = "Work with account image file")
public class StorageController {

    private final UsersService usersService;

    @UpdateOnlineTime
    @PostMapping(value = "/storage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonRs<StorageDataRs> storage(@RequestParam(value = "file",required = true) MultipartFile photo) throws IOException, FileException {
        return usersService.storeImage(photo);
    }
}
