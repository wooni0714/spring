package wooni.spring.httpclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import wooni.spring.httpclient.httpInterface.HttpInterfaceClient;

@Configuration
public class HttpClientConfig {

    private static final Logger log = LoggerFactory.getLogger(HttpClientConfig.class);

    private static final String BASEURL = "https://jsonplaceholder.typicode.com";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(BASEURL)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(BASEURL)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public HttpInterfaceClient httpClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(BASEURL)
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) ->
                        log.error("status : {}", response.getStatusCode())
                ))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(HttpInterfaceClient.class);
    }
}