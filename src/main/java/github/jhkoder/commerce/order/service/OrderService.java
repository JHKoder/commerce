package github.jhkoder.commerce.order.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderProduct;
import github.jhkoder.commerce.order.repository.OrderProductRepository;
import github.jhkoder.commerce.order.repository.OrderRepository;
import github.jhkoder.commerce.order.service.request.OrderAddRequest;
import github.jhkoder.commerce.order.service.request.OrderPayRequest;
import github.jhkoder.commerce.order.service.response.OrderInfoResponse;
import github.jhkoder.commerce.order.service.response.OrderPayResponse;
import github.jhkoder.commerce.order.service.response.OrderResponse;
import github.jhkoder.commerce.payment.domain.Payment;
import github.jhkoder.commerce.payment.repository.PaymentRepository;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;

    // 주문 요청
    @Transactional
    public Long create(String username, OrderAddRequest request) {
        var user = findUser(username);
        var itemProduct = findItem(request);

        var order = createOrder(user);
        createOrderProduct(request, itemProduct, order);

        return order.getId();
    }

    private OrderProduct createOrderProduct(OrderAddRequest request, ItemProduct itemProduct, Order order) {
        return orderProductRepository.save(request.toOrderProduct(order, itemProduct));
    }

    private Order createOrder(User user) {
        return orderRepository.save(new Order(user));
    }

    private ItemProduct findItem(OrderAddRequest request) {
        return productRepository.findById(request.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_PRODUCT));
    }

    private User findUser(String username) {
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public OrderInfoResponse findUserOrderInfo(String username, Long orderId) {

        User user = findUser(username);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));

        validOrderPay(order, user.getId());

        OrderProduct orderProduct = orderProductRepository.findById(order.getId())
                .orElseThrow();

        return OrderInfoResponse.of(user, orderProduct.getProduct(), orderProduct, order);
    }

    @Transactional
    public OrderPayResponse pay(String username, OrderPayRequest request) {
        User user = findUser(username);

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow();

        validOrderPay(order, user.getId());

        Payment payment = paymentRepository.save(request.ofPayment(order));
        order.updatePay(payment, request.receiptId(), request.zipCode(), request.address(), request.detailedAddress(), request.referenceItems());
        return new OrderPayResponse(order.getId(), order.getReceiptId());
    }

    private void validOrderPay(Order order, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new ApiException(ErrorCode.NOT_USER_ISSUED_ORDER);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse info(String username, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow();

        User user = findUser(username);

        List<OrderProduct> orderProducts = orderProductRepository.findByOrder(order)
                        .orElseThrow();

        System.out.println("service = item size:" + orderProducts.size());

        return OrderResponse.of(user, order, orderProducts, order.getPayment());
    }
}