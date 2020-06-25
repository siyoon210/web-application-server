package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticFileResponse implements Response {
    private static final Logger log = LoggerFactory.getLogger(StaticFileResponse.class);

    private final int status;
    private final Map<String, String> headers;
    private final byte[] body;

    private StaticFileResponse(int status, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void write(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        writeHeader(dos);
        writeBody(dos);
    }

    private void writeHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + status + " \r\n");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static class Builder {
        private int status;
        private final Map<String, String> headers;
        private byte[] body;

        public Builder() {
            headers = new HashMap<>();
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder header(String key, int value) {
            this.headers.put(key, String.valueOf(value));
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public StaticFileResponse build() {
            return new StaticFileResponse(status, headers, body);
        }
    }
}
