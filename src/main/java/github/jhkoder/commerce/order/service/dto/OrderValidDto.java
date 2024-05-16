package github.jhkoder.commerce.order.service.dto;

import github.jhkoder.commerce.order.domain.Order;

public record OrderValidDto(Order order, boolean isValid) {
}
