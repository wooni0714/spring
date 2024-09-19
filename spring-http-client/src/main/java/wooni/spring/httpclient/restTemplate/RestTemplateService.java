package wooni.spring.httpclient.restTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public String restTemplateEx(String num) {
        String url = "https://jsonplaceholder.typicode.com/posts/{num}";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, num);
        return response.getBody();
    }
}