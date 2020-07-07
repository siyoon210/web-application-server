package webserver;

import controller.model.HttpRequest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class HttpRequestTest {
    private HttpRequest httpRequest;

    @BeforeEach
    public void init() throws IOException {
        //given
        final String request = "POST /user/create HTTP/1.1\r\n"
                + "Host: localhost:8080\r\n"
                + "Connection: keep-alive\r\n"
                + "Content-Length: 46\n"
                + "Content-Type: application/x-www-form-urlencoded\n"
                + "Accept: */*\n"
                + "\n"
                + "userId=puru&password=1234&name=siyoon";

        final InputStream in = new ByteArrayInputStream(request.getBytes());

        //when
        this.httpRequest = HttpRequest.from(in);
    }

    @Test
    @DisplayName("HTTP요청에서 요청라인(HTTP메서드와 경로)를 파싱한다.")
    public void getHttpMethodFromRequest() throws Exception {
        assertThat(httpRequest.getMethod()).isEqualTo("POST");
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
    }

    @Test
    @DisplayName("HTTP요청에서 헤더정보를 파싱한다.")
    public void getHttpHeaderInfosFromRequest() throws Exception {
        assertThat(httpRequest.get("Host")).isEqualTo("localhost:8080");
        assertThat(httpRequest.get("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.get("Content-Length")).isEqualTo("46");
        assertThat(httpRequest.get("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
        assertThat(httpRequest.get("Accept")).isEqualTo("*/*");
    }

    @Test
    @DisplayName("HTTP요청에서 바디정보를 파싱한다.")
    public void getHttpBodyInfosFromRequest() throws Exception {
        final Map<String, String> body = httpRequest.getParsedBody();
        assertThat(body).isNotNull();
        assertThat(body.get("userId")).isEqualTo("puru");
        assertThat(body.get("password")).isEqualTo("1234");
        assertThat(body.get("name")).isEqualTo("siyoon");
    }
}
