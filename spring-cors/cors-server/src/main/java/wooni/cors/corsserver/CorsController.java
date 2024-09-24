package wooni.cors.corsserver;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @CrossOrigin(origins = "http://localhost:6060")
    @GetMapping("hi")
    public String hi() {
        return "hi";
    }
}
