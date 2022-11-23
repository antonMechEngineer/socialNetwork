package main.AOP.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.AOP.logging.dto.LoadPathDto;
import main.errors.FileAlreadyUploadException;
import main.errors.UnauthorizedException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class LogsStorage {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String clientId = "12ea62f27076416cba3d89f7bbf5046e";
    private final String token = "y0_AgAAAABmXrQEAAixEgAAAADUt4an1A5J737iQj-h8tddar39fPxBIgQ";

    @Scheduled(cron = "0 0 4 * * *")
    private void copyDir() throws IOException, FileAlreadyUploadException, UnauthorizedException {

        File logs = new File("log");
        File[] logFiles = logs.listFiles();

        assert logFiles != null;
        for (File file : logFiles) {
            try {
                String urlForUpload  = getUrlForUpload(file);
                byte[] bytesFile = Files.readAllBytes(Path.of("log/" + file.getName()));
                uploadFile(urlForUpload, bytesFile);
            } catch (HttpResponseException ex) {
                exceptionHandler(ex, file);
            }
        }
    }

    private String getUrlForUpload(File file) throws IOException {
        String getUrl = "https://cloud-api.yandex.net/v1/disk/resources/upload?path=disk:/" + file.getName() + "/";
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

    private void exceptionHandler(HttpResponseException ex, File file) throws UnauthorizedException, FileAlreadyUploadException {
        if (ex.getStatusCode() == 409) {
            throw new FileAlreadyUploadException("Log file: '" + file.getName() + "' already upload to Yandex.Disk");
        } else if (ex.getStatusCode() == 401) {
            throw new UnauthorizedException("Upload log file fail with token: " + token);
        } else {
            ex.printStackTrace();
        }
    }
}
