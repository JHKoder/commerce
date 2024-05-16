package github.jhkoder.commerce.flatform.local.ep.itemproduct.web.view;

import github.jhkoder.commerce.order.service.OrderService;
import github.jhkoder.commerce.order.service.dto.OrderValidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ProductController {

    private final OrderService orderService;

    @GetMapping("/categorys")
    public String category(){
        return "shop";
    }

    @GetMapping("/categorys/{categoryId}")
    public String category(@PathVariable Long categoryId) {
        return "shop";
    }

    @GetMapping("/products/{productId}/orders/{orderId}")
    public String productPay(@PathVariable Long productId, @PathVariable Long orderId){
       return "pay";
    }

    @GetMapping("/products/{productId}")
    public String product(@PathVariable Long productId){
        return "product";
    }

}

