package github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response;

import java.util.List;

public record ProductsCategoryPageResponse(
        Long productId,
        String imgLink,
        int price,
        int clickCount,
        int deliveryPrice
) {


}
