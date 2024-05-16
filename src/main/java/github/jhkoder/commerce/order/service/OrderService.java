package github.jhkoder.commerce.order.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderStatus;
import github.jhkoder.commerce.order.repository.OrderDslRepository;
import github.jhkoder.commerce.order.repository.OrderRepository;
import github.jhkoder.commerce.order.service.dto.OrderValidDto;
import github.jhkoder.commerce.order.service.request.OrderAddRequest;
import github.jhkoder.commerce.order.service.response.OrderAddResponse;
import github.jhkoder.commerce.order.service.response.OrderDeliveryListResponse;
import github.jhkoder.commerce.order.service.response.OrderInfoResponse;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDslRepository orderDslRepository;
    private final UserRepository userRepository;
    private final ItemProductRepository productRepository;

    // 주문 요청
    @Transactional
    public Long request(String username, OrderAddRequest request) {

        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        ItemProduct itemProduct = productRepository.findById(request.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_PRODUCT));

        Order order = orderRepository.save(Order.ofNew(itemProduct, user, request.quantity()));

        return order.getId();
    }

    @Transactional(readOnly = true)
    public boolean isOrderPaid(Long productId, Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return order.equalsProductId(productId) && !order.getStatus().equals(OrderStatus.STATUS_WAIT);
        }
        return false;
    }

    @Transactional(readOnly = true)
    public OrderInfoResponse findUserOrderInfo(String username,Long orderId) {

        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));

        if(!order.getUser().getId().equals(user.getId())){
            throw new ApiException(ErrorCode.NOT_USER_ISSUED_ORDER);
        }

        return OrderInfoResponse.of(user,order.getProduct(),order);
    }


    @Transactional(readOnly = true)
    public List<OrderDeliveryListResponse> findByUserIdGetAddress(String username){
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        var list = orderDslRepository.findByUserLimit(user,30L);
        return OrderDeliveryListResponse.of(list);
    }
}