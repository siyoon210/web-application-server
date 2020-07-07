package webserver;

import controller.model.HttpRequest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpRequestTest {
    @Test
    public void request_POST() throws Exception {
        //given
        final String request = "POST /user/create HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Contetn-Length: 46\n"
                + "Content-Type: application/x-www-form-urlencoded\n"
                + "Accept: */*\n"
                + "\n"
                + "userId=puru&password=passoword&name=siyoon";

        final InputStream in = new ByteArrayInputStream(request.getBytes());

        //when
        final HttpRequest httpRequest = HttpRequest.from(in);

        //then
        assertThat(httpRequest.getMethod(), is("POST"));
    }
}
