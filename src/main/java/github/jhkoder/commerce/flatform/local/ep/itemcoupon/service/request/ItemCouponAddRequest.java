package github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.request;

import java.time.LocalDateTime;

public record ItemCouponAddRequest(Long itemProductId, Long itemId, Long categoryId, int discountAmount , LocalDateTime startTime,LocalDateTime endTime) {
}
