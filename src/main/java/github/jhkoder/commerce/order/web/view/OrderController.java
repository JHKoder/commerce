package github.jhkoder.commerce.order.web.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    @GetMapping("/{orderId}")
    public String orderPage(@PathVariable Long orderId){
        return "order";
    }
}
