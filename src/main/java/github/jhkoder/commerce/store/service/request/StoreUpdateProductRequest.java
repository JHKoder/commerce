package github.jhkoder.commerce.store.service.request;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.image.service.request.ImageUpdateRequest;

import java.util.List;

public record StoreUpdateProductRequest(StoreSellerItem item, StoreSellerProduct product, List<ImageUpdateRequest> file) {
    public record StoreSellerItem( String maker, String origin, String brand,
                                  boolean brandCertification, String barcode) {
    }

    public record StoreSellerProduct(String name,int price,int normalPrice,  boolean orderMode, String rentalInfo,
                                     int clickCount, int reviewCount, int minimumPurchaseQuantity,
                                     String optionDetail, Gender gender, int deliveryPrice,
                                     String shippingSetting, boolean fastDelivery, boolean regularDelivery, boolean dawnDelivery,
                                     String isbn, int stock) {
    }
}
