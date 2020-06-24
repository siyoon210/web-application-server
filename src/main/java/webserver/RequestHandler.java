package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            Map<String, String> requestMessage = new HashMap<>();
            String line;
            while (!"".equals(line = bufferedReader.readLine())) {
                if (Objects.isNull(line)) {
                    break;
                }

                if (requestMessage.isEmpty()) {
                    final String[] s = line.split(" ");
                    requestMessage.put("Method", s[0]);
                    requestMessage.put("Path", s[1]);
                    requestMessage.put("Version", s[2]);
                    continue;
                }
                
                final int i = line.indexOf(":");
                requestMessage.put(line.substring(0, i), line.substring(i + 2));
            }

            for (Map.Entry<String, String> stringStringEntry : requestMessage.entrySet()) {
                log.info("key '{}' value '{}'", stringStringEntry.getKey(), stringStringEntry.getValue());
            }

            final byte[] body = Files.readAllBytes(new File("./webapp" + requestMessage.get("Path")).toPath());

            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
