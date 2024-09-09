package wooni.spring.aop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooni.spring.aop.config.LogExecution;
import wooni.spring.aop.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping({"/order"})
    public String orderInfo(@RequestParam String product, @RequestParam int quantity) {
        return this.orderService.orderInfo(product, quantity);
    }

    @LogExecution
    @GetMapping({"/order2"})
    public String orderInfo2(@RequestParam String product, @RequestParam int quantity) {
        return this.orderService.orderInfo(product, quantity);
    }
}
