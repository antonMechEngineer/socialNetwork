package main.controller;

import lombok.RequiredArgsConstructor;
import main.AOP.annotations.UpdateOnlineTime;
import main.api.response.*;
import main.service.UsersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StorageController {

    private final UsersService usersService;

    @UpdateOnlineTime
    @PostMapping(value = "/storage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<StorageRs> storage(@RequestParam(value = "file",required = false) MultipartFile photo) throws IOException {
        return ResponseEntity
                .ok(usersService.storeImage(photo));
    }
}
