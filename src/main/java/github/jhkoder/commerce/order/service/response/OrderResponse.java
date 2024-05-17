package github.jhkoder.commerce.order.service.response;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderProduct;
import github.jhkoder.commerce.payment.domain.Payment;
import github.jhkoder.commerce.user.domain.User;

import java.util.ArrayList;
import java.util.List;

import static github.jhkoder.commerce.order.service.response.OrderResponse.OrderProductRes.ofList;

public record OrderResponse(  String date,
                              Long orderNumber,
                              List<OrderProductRes> orderProducts,
                              String recipientName,
                              String recipientPhoneNumber,

                              String address,
                              String detailedAddress,
                              String referenceItems,
                              String zipcode,

                              int actualPaymentAmount,
                              String paymentMethod) {

    public static OrderResponse of(User user, Order order, List<OrderProduct> orderProducts, Payment payment) {
        return new OrderResponse(
                payment.getCreateTime().toString(),
                order.getId(),
                ofList(orderProducts),
                user.getUserName(),
                user.getPhone(),
                order.getAddress(),
                order.getDetailedAddress(),
                order.getReferenceItems(),
                order.getZipCode(),
                payment.getActualPaymentAmount(),
                payment.getPaymentMethod()
        );
    }
    public record OrderProductRes(String productImageUrl,
                                 String storeName,
                                 String productName,
                                 String productOption,
                                 int productPrice,
                                  int deliveryPrice,
                                  int quantity){
        public static OrderProductRes of(OrderProduct orderProduct){
            ItemProduct itemProduct = orderProduct.getProduct();
            return new OrderProductRes(itemProduct.getMainImage().getPath(),
                   "상호A",
                    itemProduct.getName(),
                    itemProduct.getOptionDetail(),
                    itemProduct.getPrice(),
                    itemProduct.getDeliveryPrice(),
                    orderProduct.getQuantity());
        }

        public static List<OrderProductRes> ofList(List<OrderProduct> orderProducts){
            List<OrderProductRes> responses = new ArrayList<>();
            for(OrderProduct orderProduct: orderProducts){
                responses.add((of(orderProduct)));
            }
            return responses;
        }

    }
}
