package dev.dogeared.zenithdex.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "vaadin.productionMode=true"
)
class SecurityConfigTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient httpClient = HttpClient.newBuilder()
        .cookieHandler(new CookieManager())
        .build();

    @Test
    void cardImagesArePubliclyReadable() throws Exception {
        HttpResponse<String> response = get("/images/cards.jpg");

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void oldBrowserFallbackIsReachable() throws Exception {
        get("/");
        HttpResponse<String> response = get("/?v-r=oldbrowser");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("browser");
    }

    @Test
    void applicationStylesheetIsPubliclyReadable() throws Exception {
        HttpResponse<String> response = get("/styles/zenithdex.css");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains(".card-browser");
        assertThat(response.body()).contains(".footer");
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + path))
            .GET()
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
