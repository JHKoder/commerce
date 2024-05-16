package github.jhkoder.commerce.order.service.response;

import github.jhkoder.commerce.order.domain.Order;
import lombok.Data;

@Data
public class OrderAddResponse {
    private final Long id;
    public OrderAddResponse(Order order) {
        this.id  =order.getId();
    }
}
