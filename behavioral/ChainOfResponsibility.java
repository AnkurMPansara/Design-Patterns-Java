/*
    This is your reminder to not read patterns in alphabetical order.
    This one is useful but too niche to be used consistently. Not important for last minute revision.
    Main purpose of this pattern is to allow dynamic component processing.
    Client can decide the order of middleware and whether they are needed or not in a pipeline.
    Rather than chain, imagine it as hotwheels track. You decide what happens and when.
    In standard pattern version, only single element of chain actually do somework.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChainOfResponsibility {
    public static void main(String[] args) {
        HTTPRequest request = new HTTPRequest();
        request.url = "https://api.example.com/v1/data";
        request.method = "POST";
        request.headers = new ArrayList<>();
        request.headers.add("Authorization: Bearer valid-key-123");

        Set<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "POST"));
        String accessKey = "valid-key-123";

        IRequestHandler logger = new RequestLogger();
        IRequestHandler urlValidator = new UrlValidator();
        IRequestHandler methodValidator = new MethodValidator(allowedMethods);
        IRequestHandler auth = new Authenticator(accessKey);

        logger.setNext(urlValidator);
        urlValidator.setNext(methodValidator);
        methodValidator.setNext(auth);

        System.out.println("--- Starting Request Pipeline ---");
        logger.handle(request);
    }
}

class HTTPRequest {
    public String url;
    public String method;
    public Map<String, String> params;
    public List<String> headers;
    public byte[] payload;
    public Integer connTimeout;
    public Integer reqTimeout;

    @Override
    public String toString() {
        return "Url:" + url + 
            " method:" + method + 
            " params:" + params + 
            " headers:" + headers +
            " payload:" + Arrays.toString(payload);
    }
}

interface IRequestHandler {
    void setNext(IRequestHandler next);
    void handle(HTTPRequest request);
}

class UrlValidator implements IRequestHandler {
    private IRequestHandler next;

    @Override
    public void setNext(IRequestHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HTTPRequest request) {
        if (request.url == null || !request.url.startsWith("https")) {
            System.out.println("Do you really think http request with url like this will be passed?");
            return;
        } else {
            System.out.println("URL is good to go");
        }
        if (next != null) {
            next.handle(request);
        }
    }
}

class MethodValidator implements IRequestHandler {
    private IRequestHandler next;
    private final Set<String> allowedMethods;

    public MethodValidator(Set<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    @Override
    public void setNext(IRequestHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HTTPRequest request) {
        if (!allowedMethods.contains(request.method)) {
            System.out.println("Nope, method " + request.method + " not allowed.");
            return;
        } else {
            System.out.println("Method is allowed");
        }
        if (next != null) {
            next.handle(request);
        }
    }
}

class Authenticator implements IRequestHandler {
    private IRequestHandler next;
    private final String accessKey;

    public Authenticator(String accessKey) {
        this.accessKey = accessKey;
    }

    @Override
    public void setNext(IRequestHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HTTPRequest request) {
        Boolean authenticated = false;
        for (String header: request.headers) {
            if (header.equals("Authorization: Bearer " + accessKey)) {
                authenticated = true;
            }
        }
        if (!authenticated) {
            System.out.println("Unauthorized request, send terminator after creator of request.");
            return;
        } else {
            System.out.println("Authenticated");
        }
        if (next != null) {
            next.handle(request);
        }
    }
}

class RequestLogger implements IRequestHandler {
    private IRequestHandler next;

    @Override
    public void setNext(IRequestHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HTTPRequest request) {
        System.out.println("Request received:\n" + request);
        if (next != null) {
            next.handle(request);
        }
    }
}