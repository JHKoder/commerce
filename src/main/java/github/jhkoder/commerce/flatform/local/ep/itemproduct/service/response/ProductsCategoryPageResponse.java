package github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response;

import github.jhkoder.commerce.image.domain.Images;
import lombok.Data;

@Data
public class ProductsCategoryPageResponse {
    private Long productId;
    private String name;
    private String mainImage;
    private Integer price;
    private Integer normalPrice;
    private Integer deliveryPrice;

    public ProductsCategoryPageResponse(Long productId, String name, String mainImage, Integer price, Integer normalPrice, Integer deliveryPrice) {
        this.productId = productId;
        this.name = name;
        this.mainImage = mainImage;
        this.price = price;
        this.normalPrice = normalPrice;
        this.deliveryPrice = deliveryPrice;
    }
}
