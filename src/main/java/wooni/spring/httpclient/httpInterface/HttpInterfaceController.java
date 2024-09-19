package wooni.spring.httpclient.httpInterface;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HttpInterfaceController {

    private final HttpInterfaceService httpInterfaceService;

    @GetMapping("/httpInterface/data/")
    public ResponseEntity<String> getData(@RequestParam String id) {
        String data = httpInterfaceService.getData(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}