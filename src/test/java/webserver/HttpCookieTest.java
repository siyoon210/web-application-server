package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.model.HttpCookie;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class HttpCookieTest {
    @Test
    @DisplayName("문자열 쿠키값으로 HttpCookie로 파싱하기")
    public void createHttpCookie() {
        //given
        String cookieAsString = "key1=value1;key2=value2";

        //when
        final HttpCookie httpCookie = new HttpCookie(cookieAsString);

        //then
        assertThat(httpCookie.get("key1")).isEqualTo("value1");
        assertThat(httpCookie.get("key2")).isEqualTo("value2");
        assertThat(httpCookie.get("key3")).isEqualTo(null);
    }

    @Test
    @DisplayName("빈 문자열 쿠키값으로 HttpCookie로 파싱하면 객체만 반환되고 아무 값도 담지 않는다.")
    public void createHttpCookieByNull() {
        //given
        String cookieAsString = null;

        //when
        final HttpCookie httpCookie = new HttpCookie(cookieAsString);

        //then
        assertThat(httpCookie).isNotNull();
    }
}
