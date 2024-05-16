package github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response;

import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;

import java.util.List;

public record ProductResponse(
        /*

         */
        String sellerName,

        Long productId,
        Long userId,
        Long categoryId,
        String mainImageUrl,
        List<String> imageContexts,
        String itemName,
        int normalPrice,
        String maker,
        String origin,
        String brand,
        boolean brandCertification,
        String barcode,


        int price,
        boolean orderMode,
        String rentalInfo,
        int clickCount,
        int reviewCount,
        int minimumPurchaseQuantity,
        String optionDetail,
        String gender,
        int deliveryPrice,
        String shippingSetting,
        boolean fastDelivery,
        boolean regularDelivery,
        boolean dawnDelivery,
        String isbn,
        int stock,
        List<String> categorys
) {
    public static ProductResponse of(ItemProduct product, Item item,String mainImage,List<String> imagesContexts,List<String> categorys) {
        return new ProductResponse(
                product.getUser().getUserName(),
                product.getId(),
                product.getUser().getId(),
                product.getCategory().getId(),
                mainImage,
                imagesContexts,
                product.getName(),
                product.getNormalPrice(),

                item.getMaker(),
                item.getOrigin(),
                item.getBrand(),
                item.getBrandCertification().bool(),
                item.getBarcode(),
                product.getPrice(),
                product.getOrderMode().bool(),
                product.getRentalInfo(),
                product.getClickCount(),
                product.getReviewCount(),
                product.getMinimumPurchaseQuantity(),
                product.getOptionDetail(),
                product.getGender().toString(),
                product.getDeliveryPrice(),
                product.getShippingSetting(),
                product.getFastDelivery().bool(),
                product.getRegularDelivery().bool(),
                product.getDawnDelivery().bool(),
                product.getIsbn(),
                product.getStock(),
                categorys
        );
    }
}