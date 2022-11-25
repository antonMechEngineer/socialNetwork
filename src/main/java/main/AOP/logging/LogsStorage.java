package main.AOP.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.AOP.logging.dto.LoadPathDto;
import main.errors.UnauthorizedException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LogsStorage {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${yandex.OAuth}")
    private String token;

    @Value("${logging.file.uploadLogs}")
    private boolean uploadLogs;

    @Scheduled(cron = "0 0 4 * * *")
    private void copyDir() throws IOException, UnauthorizedException {
        if (!uploadLogs) {
            return;
        }

        File logs = new File("log");
        File[] logFiles = logs.listFiles();

        assert logFiles != null;
        for (File file : logFiles) {
            try {
                String urlForUpload  = getUrlForUpload(file);
                byte[] bytesFile = Files.readAllBytes(Path.of("log/" + file.getName()));
                uploadFile(urlForUpload, bytesFile);
            } catch (HttpResponseException ex) {
                exceptionHandler(ex);
            }
        }
    }

    private String getUrlForUpload(File file) throws IOException {
        String getUrl = "https://cloud-api.yandex.net/v1/disk/resources/upload?path=disk:/" + file.getName() + "/&overwrite=true";
        Object url = Request.Get(getUrl)
                .setHeader("Authorization", token)
                .execute()
                .returnContent();
        LoadPathDto loadPathDto = objectMapper.readValue(url.toString(), LoadPathDto.class);

        return loadPathDto.getHref();
    }

    private void uploadFile(String urlForUpload, byte[] file) throws IOException {
        Request.Put(urlForUpload)
                .bodyByteArray(file)
                .execute()
                .returnContent();
    }

    private void exceptionHandler(HttpResponseException ex) throws UnauthorizedException {
        if (ex.getStatusCode() == 401) {
            throw new UnauthorizedException("Fail upload log file fail with token: " + token);
        } else {
            ex.printStackTrace();
        }
    }
}
