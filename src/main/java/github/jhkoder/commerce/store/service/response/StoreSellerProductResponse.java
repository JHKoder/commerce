package github.jhkoder.commerce.store.service.response;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;

public record StoreSellerProductResponse(StoreSellerItem item, StoreSellerProduct product) {

    public record StoreSellerItem(String name, int normalPrice, String maker, String origin, String brand,
                                    boolean brandCertification, String barcode) { }

    public record StoreSellerProduct(
            int price, boolean orderMode, String rentalInfo,
            int clickCount,int reviewCount, int minimumPurchaseQuantity,
            String optionDetail, Gender gender, int deliveryPrice,
            String shippingSetting, boolean fastDelivery, boolean regularDelivery, boolean dawnDelivery,
            String isbn, int stock) { }

    public static StoreSellerProductResponse of(ItemProduct product) {
        return new StoreSellerProductResponse(
                new StoreSellerItem(
                        product.getItem().getName(),
                        product.getItem().getNormalPrice(),
                        product.getItem().getMaker(),
                        product.getItem().getOrigin(),
                        product.getItem().getBrand(),
                        product.getItem().getBrandCertification().bool(),
                        product.getItem().getBarcode()
                ),
                new StoreSellerProduct(
                        product.getPrice(),
                        product.getOrderMode().bool(),
                        product.getRentalInfo(),
                        product.getClickCount(),
                        product.getReviewCount(),
                        product.getMinimumPurchaseQuantity(),
                        product.getOptionDetail(),
                        product.getGender(),
                        product.getDeliveryPrice(),
                        product.getShippingSetting(),
                        product.getFastDelivery().bool(),
                        product.getRegularDelivery().bool(),
                        product.getDawnDelivery().bool(),
                        product.getIsbn(),
                        product.getStock()
                )
        );
    }
}
