/*
    We got our bob the builder here, the Builder Pattern.
    This one is very simple, It allows you to build an object at your pace that must be contructed at once with all parameters.
    With this builder class you can use polymorphism to support multiple ways of setting a param or leave it as default.
    This one helps with DRY principle more than SOLID principles.
 */

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Builder {
    public static void main(String[] args) {
        HttpRequest getRequest = new RequestBuilder()
                .setUrl("https://api.example.com/v1/data")
                .setMethod("GET")
                .setHeaders(Arrays.asList("Authorization: Bearer TOKEN_123", "Accept: application/json"))
                .setParams(Collections.singletonMap("id", "42"))
                .build();
        getRequest.executeRequest();

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John Doe");
        userData.put("role", "Admin");
        HttpRequest postRequest = new RequestBuilder()
                .setUrl("https://api.example.com/v1/users")
                .setMethod("POST")
                .setPayload(userData)
                .setTimeout(5000)
                .build();
        postRequest.executeRequest();
    }
}

class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> params;
    private final List<String> headers;
    private final byte[] payload;
    private final Integer connTimeout;
    private final Integer reqTimeout;

    public HttpRequest(String url, String method, Map<String, String> params, List<String> headers, byte[] payload, Integer connTimeout, Integer reqTimeout) {
        this.url = url;
        this.method = method;
        this.params = params;
        this.headers = headers;
        this.payload = payload;
        this.connTimeout = connTimeout;
        this.reqTimeout = reqTimeout;
    }

    public void executeRequest() {
        System.out.println("If you reall think i would really hit this req, high is stupidity in you");
        System.out.println("--- Debugging HttpRequest ---");
        System.out.println("URL: " + url);
        System.out.println("Method: " + method);
        
        // Printing Map parameters
        System.out.print("Params: ");
        if (params != null && !params.isEmpty()) {
            params.forEach((k, v) -> System.out.print(k + "=" + v + " "));
        } else {
            System.out.print("None");
        }
        System.out.println("Headers: " + (headers != null ? headers : "None"));
        if (payload != null) {
            System.out.println("I aint printing the payload, size of it is " + payload.length + " bytes");
        }
        System.out.println("Connection Timeout: " + connTimeout + "ms");
        System.out.println("Request Timeout: " + reqTimeout + "ms");
        System.out.println("-----------------------------");
    }
}

class RequestBuilder {
    private String url;
    private String method;
    private Map<String, String> params;
    private List<String> headers;
    private byte[] payload;
    private Integer connTimeout = 200;
    private Integer reqTimeout = 1000; 

    public RequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public RequestBuilder setHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder setPayload(Map<String, Object> payload) {
        String json = "{" + payload.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"")
            .collect(Collectors.joining(",")) + "}";
        this.payload = json.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public RequestBuilder setPayload(String payload) {
        this.payload = payload.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public RequestBuilder setTimeout(Integer connTimeout, Integer reqTimeout) {
        this.connTimeout = connTimeout;
        this.reqTimeout = reqTimeout;
        return this;
    }

    public RequestBuilder setTimeout(Integer timeout) {
        this.connTimeout = timeout;
        this.reqTimeout = timeout;
        return this;
    }

    public HttpRequest build() {
        return new HttpRequest(url, method, params, headers, payload, connTimeout, reqTimeout);
    }
}