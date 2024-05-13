package github.jhkoder.commerce.store.service.request;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.user.domain.User;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




public record StoreAddProductRequest(StoreItemRequest item, StoreProductRequest product, CustomMultiPartFile images,MultipartFile mainImage) {

    public record StoreItemRequest( String maker, String origin, String brand,
                                   boolean brandCertification, String barcode) {

        public Item toItem() {
            return new Item(maker, origin, brand, brandCertification, barcode);
        }
    }

    public record StoreProductRequest(Long categoryId,String name, int price, int normalPrice, boolean orderMode, String rentalInfo, int clickCount, int reviewCount, int minimumPurchaseQuantity,
                                      String optionDetail, Gender gender, int deliveryPrice, String shippingSetting,
                                      boolean fastDelivery, boolean regularDelivery, boolean dawnDelivery,
                                      String isbn, int stock
    ) {


        public ItemProduct toProduct(User user, Category category, Item item) {
            return new ItemProduct(user, category, item, new ArrayList<>(),null, name, price,normalPrice, orderMode, rentalInfo, clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice, shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
        }
    }


    @Data
    public static class  CustomMultiPartFile implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private List<MultipartFile> image;

        public CustomMultiPartFile() {
            this.image = new ArrayList<>();
        }

        public void add(MultipartFile image) {
            this.image.add(image);
        }
    }

}
