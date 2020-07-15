package webserver;

import webserver.model.HttpRequest;
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
        final String request = "POST /user/create HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Content-Length: 46\n"
                + "Content-Type: application/x-www-form-urlencoded\n"
                + "Accept: */*\n"
                + "Cookie: isLogined=false;cookie2=value2\n"
                + "\n"
                + "userId=puru&password=1234&name=siyoon";

        final InputStream in = new ByteArrayInputStream(request.getBytes());

        //when
        this.httpRequest = HttpRequest.from(in);
    }

    @Test
    @DisplayName("HTTP요청에서 요청라인(HTTP메서드와 경로)를 파싱한다.")
    public void getHttpMethodFromRequest() {
        assertThat(httpRequest.getMethod()).isEqualTo("POST");
        assertThat(httpRequest.getPath()).isEqualTo("/user/create");
    }

    @Test
    @DisplayName("HTTP요청에서 헤더정보를 파싱한다.")
    public void getHttpHeaderInfosFromRequest() {
        assertThat(httpRequest.get("Host")).isEqualTo("localhost:8080");
        assertThat(httpRequest.get("Connection")).isEqualTo("keep-alive");
        assertThat(httpRequest.get("Content-Length")).isEqualTo("46");
        assertThat(httpRequest.get("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
        assertThat(httpRequest.get("Accept")).isEqualTo("*/*");
    }

    @Test
    @DisplayName("HTTP요청에서 쿠키정보를 파싱한다.")
    public void getHttpCookieInfosFromRequest() {
        final Map<String, String> cookies = httpRequest.getCookies();
        assertThat(cookies).isNotNull();
        assertThat(cookies.get("isLogined")).isEqualTo("false");
        assertThat(cookies.get("cookie2")).isEqualTo("value2");
    }

    @Test
    @DisplayName("HTTP요청에서 바디정보를 파싱한다.")
    public void getHttpBodyInfosFromRequest() {
        final Map<String, String> body = httpRequest.getParsedBody();
        assertThat(body).isNotNull();
        assertThat(body.get("userId")).isEqualTo("puru");
        assertThat(body.get("password")).isEqualTo("1234");
        assertThat(body.get("name")).isEqualTo("siyoon");
    }
}
