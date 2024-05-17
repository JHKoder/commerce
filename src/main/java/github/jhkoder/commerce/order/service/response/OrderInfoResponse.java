package github.jhkoder.commerce.order.service.response;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderProduct;
import github.jhkoder.commerce.user.domain.User;

public record OrderInfoResponse(Long orderId,String name, String phone, String email,
                                String mainImage,String itemName,int quantity, String option,int price,int normalPrice ,int deliveryPrice){

    public static OrderInfoResponse of(User user, ItemProduct itemProduct, OrderProduct orderProduct, Order order) {
        return new OrderInfoResponse(order.getId(),user.getUserName(), user.getPhone(),user.getEmail(),
                itemProduct.getMainImage().getPath() ,itemProduct.getName(),orderProduct.getQuantity(),itemProduct.getOptionDetail(),itemProduct.getPrice(),itemProduct.getNormalPrice(),itemProduct.getDeliveryPrice()
        );
    }
}
