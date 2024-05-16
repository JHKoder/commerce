package github.jhkoder.commerce.order.web.rest;

import github.jhkoder.commerce.order.service.OrderService;
import github.jhkoder.commerce.order.service.request.OrderAddRequest;
import github.jhkoder.commerce.order.service.response.OrderInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService service;

    @PostMapping
    public Long orderRequest(@AuthenticationPrincipal String username, @Valid @RequestBody OrderAddRequest request) {
        return service.request(username,request);
    }

    @GetMapping("/info/{orderId}")
    public OrderInfoResponse view(@AuthenticationPrincipal String username,@PathVariable Long orderId) {
        return service.findUserOrderInfo(username,orderId);
    }


}
