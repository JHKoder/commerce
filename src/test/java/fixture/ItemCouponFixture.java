package fixture;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;

import java.time.LocalDateTime;

public class ItemCouponFixture {

    public static ItemCoupon getItemCouponFixture(ItemProduct product, Item item, Category category){
        return new ItemCoupon(product,category, 1, LocalDateTime.now(),LocalDateTime.now().plusDays(1));
    }
}
