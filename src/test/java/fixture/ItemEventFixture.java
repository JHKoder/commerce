package fixture;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemevent.domain.ItemEvent;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;

import java.time.LocalDateTime;

public class ItemEventFixture {

    public static ItemEvent getItemEventFixture(ItemProduct product, Item item, Category category){
        return new ItemEvent(product,category,0,"",false,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
    }
    public static ItemEvent getItemEventFixtureAndCoupon(ItemProduct product, Item item, Category category){
        return new ItemEvent(product,category,0,"",true,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
    }
}
