package http.response;

import http.HTTP;
import http.HttpCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.HttpSession;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponse implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String NEW_LINE = "\r\n";

    private StatusLine statusLine;
    private ResponseHeader header;
    private ResponseBody body;
    private HttpSession httpSession;

    private final DataOutputStream dataOutputStream;

    public HttpResponse(final OutputStream out) {
        this.statusLine = new StatusLine();
        this.header = new ResponseHeader();
        this.body = new ResponseBody();
        this.dataOutputStream = new DataOutputStream(out);
    }

    public void addStatusCode(ResponseStatus responseStatus) {
        this.statusLine = new StatusLine(responseStatus);
    }

    public void addHeader(Map<HTTP, String> header) {
        this.header.add(header);
    }

    public void addHeader(HTTP http, String value) {
        header.addContents(http, value);
    }

    public void addCookie(HttpCookie httpCookie) {
        header.addCookie(httpCookie);
    }

    public void addBody(byte[] body) {
        this.body = new ResponseBody(body);
    }

    public String getHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append(statusLine.getResponse()).append(NEW_LINE);
        sb.append(header.getResponse()).append(NEW_LINE);

        return sb.toString();
    }

    public String getBody() {
        return new String(body.getContents());
    }

    public void write() throws IOException {
        writeHeader();
        writeBody();
    }

    @Override
    public void close() throws IOException {
        dataOutputStream.close();
    }

    private void writeBody() throws IOException {
        dataOutputStream.write(body.getContents(), 0, body.length());
        dataOutputStream.flush();
    }

    private void writeHeader() throws IOException {
        dataOutputStream.writeBytes(getHeader());
    }

    public String getHeaderContents(HTTP http) {
        return header.getContents(http);
    }

    public void addSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public HttpSession getSession() {
        return httpSession;
    }
}
