package github.jhkoder.commerce.order.web.rest;

import github.jhkoder.commerce.order.service.OrderService;
import github.jhkoder.commerce.order.service.request.OrderAddRequest;
import github.jhkoder.commerce.order.service.request.OrderPayRequest;
import github.jhkoder.commerce.order.service.response.OrderInfoResponse;
import github.jhkoder.commerce.order.service.response.OrderPayResponse;
import github.jhkoder.commerce.order.service.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/order")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService service;

    // 상품 한개          구매시
    @PostMapping
    public Long orderRequest(@AuthenticationPrincipal String username, @Valid @RequestBody OrderAddRequest request) {
        return service.create(username,request);
    }

    // 상품 여러개 장방구니 구매시 장바구니 예정
    // @PostMapping("/장방구니")

    @PostMapping("/pay")
    public OrderPayResponse orderPay(@AuthenticationPrincipal String username, @Valid @RequestBody OrderPayRequest request) {
        return service.pay(username,request);
    }

    // 상품 상세보기 에서 구매시 주문 정보 획득
    @GetMapping("/{orderId}")
    public OrderInfoResponse view(@AuthenticationPrincipal String username,@PathVariable Long orderId) {
        return service.findUserOrderInfo(username,orderId);
    }

    // 주문 상세 정보 (영수증 ) 정보 전달
    @GetMapping("/{orderId}/info")
    public OrderResponse orderDetails(@AuthenticationPrincipal String username,@PathVariable Long orderId){

        return service.info(username,orderId);
    }


}
