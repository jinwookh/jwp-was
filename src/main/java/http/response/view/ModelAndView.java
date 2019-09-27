package http.response.view;

import com.google.common.collect.Maps;
import http.ContentType;
import http.HTTP;
import http.response.ResponseStatus;

import java.io.IOException;
import java.util.Map;

public class ModelAndView implements View {
    private final Map<HTTP, String> header = Maps.newHashMap();
    private final byte[] body;

    public ModelAndView(String path, Map<String, Object> model) throws IOException {
        this.body = createBody(path, model, new HandlebarResolver());
        header.put(HTTP.CONTENT_TYPE, ContentType.valueByPath(path).getContents() + ";charset=utf-8");
        header.put(HTTP.CONTENT_LENGTH, String.valueOf(body.length));
    }

    public ModelAndView(String path, Map<String, Object> model, TemplateResolver templateResolver) throws IOException {
        this.body = createBody(path, model, templateResolver);
    }

    private byte[] createBody(String path, Map<String, Object> model, TemplateResolver templateResolver) throws IOException {
        return templateResolver.getBody(path, model).getBytes();
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return ResponseStatus.OK;
    }

    @Override
    public Map<HTTP, String> getHeader() {
        return header;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
