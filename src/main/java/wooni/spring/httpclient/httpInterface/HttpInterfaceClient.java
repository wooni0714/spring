package wooni.spring.httpclient.httpInterface;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange("/posts")
public interface HttpInterfaceClient {

    @GetExchange("/")
    String getData(@RequestParam String id);
}