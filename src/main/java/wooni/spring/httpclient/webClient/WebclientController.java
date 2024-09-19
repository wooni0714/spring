package wooni.spring.httpclient.webClient;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class WebclientController {

    private final WebclientService webclientService;

    @GetMapping("/webclient/data")
    public Mono<String> getData(@RequestParam String num) {
        return webclientService.getData(num);
    }

    @GetMapping("/webclient/dat")
    public Flux<String> getAllData() {
        return webclientService.getAllData();
    }
}