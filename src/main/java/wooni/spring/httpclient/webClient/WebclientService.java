package wooni.spring.httpclient.webClient;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wooni.spring.httpclient.exception.CustomException;

@Service
@RequiredArgsConstructor
public class WebclientService {
    private static final Logger log = LoggerFactory.getLogger(WebclientService.class);

    private final WebClient webClient;

    public Mono<String> getData(String num) {
        return webClient.get()
                .uri("/posts/{num}", num)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Client Error: Status Code - {}", clientResponse.statusCode());
                    return Mono.error(new CustomException("Client Error"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Server Error: Status Code - {}", clientResponse.statusCode());
                    return Mono.error(new CustomException("Server Error"));
                })
                .bodyToMono(String.class)
                .doOnError(error -> log.error("Error: {}", error.getMessage()));
    }

    public Flux<String> getAllData() {
        return webClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(error -> log.error("Error : {}", error.getMessage()));
    }
}