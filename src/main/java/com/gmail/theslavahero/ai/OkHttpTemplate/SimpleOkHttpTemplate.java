package com.gmail.theslavahero.ai.OkHttpTemplate;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Yevhenii Filatov
 * @since 11/27/20
 **/

@Slf4j
@Service
public class SimpleOkHttpTemplate implements OkHttpTemplate {

    private final OkHttpClient okHttpClient;

    public SimpleOkHttpTemplate(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public String request(Request request) throws IOException {
        return doGetStringBodyTemplate(request);
    }

    @Override
    public byte[] requestBinary(Request request) throws IOException {
        return doGetBinaryBodyTemplate(request);
    }

    @Override
    public Response rawRequest(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }

    private String doGetStringBodyTemplate(Request request) throws IOException {
        String url = request.url().toString();
        Response response = okHttpClient.newCall(request).execute();
        try (ResponseBody body = response.body()) {
            if (body == null) {
                log.error("EMPTY REQUEST BODY RETURNED FOR {}", url);
                log.error("RESPONSE MESSAGE: {}", response.message());
                throw new IOException("No content returned for " + request.method() + " " + request.url());
            }

            if (!response.isSuccessful()) {
                log.error("REQUEST FAILED - {}", url);
                log.error("STATUS CODE: {}", response.code());
                log.error("RESPONSE MESSAGE: {}", response.message());
                log.error("RESPONSE BODY: {}", body.string());
                throw new IOException(response.message(), new IOException(request.method() + " " + request.url() + ": " + response.code()));
            }
            return body.string();
        }
    }

    private byte[] doGetBinaryBodyTemplate(Request request) throws IOException {
        Response response = okHttpClient.newCall(request).execute();
        try (ResponseBody body = response.body()) {
            if (body == null)
                throw new IOException("No content returned for " + request.method() + " " + request.url());

            if (!response.isSuccessful())
                throw new IOException(response.message(), new IOException(request.method() + " " + request.url()));
            return body.bytes();
        }
    }

}
