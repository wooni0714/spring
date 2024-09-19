package wooni.spring.httpclient.restTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    @GetMapping("/restTemplate/get")
    public ResponseEntity<String> getExternalData(@RequestParam String num) {
        String data = restTemplateService.restTemplateEx(num);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}