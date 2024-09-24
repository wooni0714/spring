package wooni.cors.corsweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CorsWebController {

    @GetMapping("/test/cors")
    public String view() {
        return "cors";
    }

    @GetMapping("/test/origins")
    public String viewOrigins() {
        return "corsOrigin";
    }
}
