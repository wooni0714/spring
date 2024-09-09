package wooni.spring.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public String orderInfo(String product, int quantity) {
        log.info("product {}, quantity {}", product, quantity);
        return "SUCCESS";
    }
}
