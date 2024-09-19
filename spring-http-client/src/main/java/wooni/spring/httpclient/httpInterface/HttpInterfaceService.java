package wooni.spring.httpclient.httpInterface;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpInterfaceService{

    private final HttpInterfaceClient httpInterfaceClient;

    public String getData(String id) {
        return httpInterfaceClient.getData(id);
    }
}