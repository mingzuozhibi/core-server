package mingzuozhibi.coreserver.test.imports;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import mingzuozhibi.coreserver.modules.disc.DiscRepository;
import mingzuozhibi.coreserver.modules.shelf.Shelf;
import mingzuozhibi.coreserver.modules.shelf.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

import static mingzuozhibi.coreserver.commons.gson.GSONs.GSON;

@RestController
public class ImportShelfs {

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private ShelfRepository shelfRepository;

    @Value("${BCLOUD_IP:127.0.0.1}")
    private String bcloudIp;

    @SneakyThrows
    @GetMapping("/import/shelfs")
    public long importShelfs() {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(String.format("http://%s:9000/discShelfs?pageSize=100", bcloudIp)))
            .build();
        HttpResponse<String> httpResponse = HttpClient.newHttpClient()
            .send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() == 200) {
            String body = httpResponse.body();
            JsonObject jsonObject = GSON.fromJson(body, JsonObject.class);
            if (jsonObject.get("success").getAsBoolean()) {
                jsonObject.get("data").getAsJsonArray().forEach(e -> {
                    JsonObject row = e.getAsJsonObject();
                    String asin = row.get("asin").getAsString();
                    String type = row.get("type").getAsString();
                    String title = row.get("title").getAsString();
                    Instant createOn = Instant.ofEpochMilli(row.get("createOn").getAsLong());

                    Shelf shelf = new Shelf(asin, type, title);
                    shelf.setCreateOn(createOn);
                    discRepository.findByAsin(asin).ifPresent(disc -> shelf.setDiscId(disc.getId()));
                    shelfRepository.save(shelf);
                });
                return shelfRepository.count();
            }
        }
        return -1;
    }

}
