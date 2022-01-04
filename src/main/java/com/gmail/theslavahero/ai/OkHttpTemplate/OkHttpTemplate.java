package com.gmail.theslavahero.ai.OkHttpTemplate;

import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @author Yevhenii Filatov
 * @since 11/27/20
 **/

public interface OkHttpTemplate {

    String request(Request request) throws IOException;

    byte[] requestBinary(Request request) throws IOException;

    Response rawRequest(Request request) throws IOException;

    /**
     * HTTP GET operation
     *
     * @param url to retrieve text content from
     * @return fetched content as text
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default String get(String url) throws IOException {
        return get(url, Collections.emptyMap());
    }

    /**
     * HTTP GET operation with headers
     *
     * @param url     to retrieve text content from
     * @param headers headers for request
     * @return fetched content as text
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default String get(String url, Map<String, String> headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .get()
                .build();
        return request(request);
    }

    /**
     * HTTP GET operation
     *
     * @param url to retrieve binary content from
     * @return fetched content as binary array
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default byte[] getBinary(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return requestBinary(request);
    }

    /**
     * HTTP POST operation
     *
     * @param url to send request for
     * @return result of request as text
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default String post(String url) throws IOException {
        return post(url, null);
    }

    /**
     * HTTP POST operation
     *
     * @param url      to send request for
     * @param formData map with post-data
     * @return result of request as text
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default String post(String url, Map<String, String> formData) throws IOException {
        return post(url, Collections.emptyMap(), formData);
    }

    /**
     * HTTP POST operation with headers
     *
     * @param url      to send request for
     * @param headers  headers for request
     * @param formData map with post-data
     * @return result of request as text
     * @throws IOException if any network or server error occurs, or no content returned
     */
    default String post(String url, Map<String, String> headers, Map<String, String> formData) throws IOException {
        RequestBody requestBody;
        if (formData == null) {
            requestBody = RequestBody.create(null, new byte[0]);
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            formData.forEach(builder::add);
            requestBody = builder.build();
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(requestBody)
                .build();
        return request(request);
    }
}

