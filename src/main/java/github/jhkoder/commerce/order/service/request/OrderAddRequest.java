package github.jhkoder.commerce.order.service.request;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderProduct;

public record OrderAddRequest(Long productId, int quantity, int discountAmount) {

    public OrderProduct toOrderProduct(Order order, ItemProduct itemProduct) {
        return new OrderProduct(order, itemProduct
                , quantity(), quantity(), discountAmount());
    }
}
