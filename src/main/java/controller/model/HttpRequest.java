package controller.model;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static util.HttpRequestUtils.parseHeader;

public class HttpRequest {
    private final Map<String, String> requestInfo;

    private HttpRequest(InputStream in) throws IOException {
        requestInfo = new HashMap<>();

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        int contentLength = parseHeaders(bufferedReader);

        if (contentLength > 0) {
            parseBody(bufferedReader, contentLength);
        }
    }

    public static HttpRequest from(InputStream in) throws IOException {
        return new HttpRequest(in);
    }

    private int parseHeaders(BufferedReader bufferedReader) throws IOException {
        String line;
        int contentLength = 0;
        while (!"".equals(line = bufferedReader.readLine())) {
            if (Objects.isNull(line)) {
                break;
            }

            if (requestInfo.isEmpty()) {
                final String[] s = line.split(" ");
                requestInfo.put("Method", s[0]);
                requestInfo.put("Path", s[1]);
                requestInfo.put("Version", s[2]);
                continue;
            }

            final HttpRequestUtils.Pair pair = parseHeader(line);
            if (Objects.isNull(pair)) {
                continue;
            }

            if (pair.getKey().equals("Content-Length")) {
                contentLength = Integer.parseInt(pair.getValue());
            }
            requestInfo.put(pair.getKey(), pair.getValue());
        }
        return contentLength;
    }

    private void parseBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        String body = IOUtils.readData(bufferedReader, contentLength);
        requestInfo.put("body", body);
    }

    public String get(String key) {
        return requestInfo.get(key);
    }
}
