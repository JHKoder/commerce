package fixture;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class ItemProductFixture {

    public ItemProduct getItemProductFixture(){
        User user = new User("testUser", "testname","password12","test@gmail.com","01012341234",Gender.MAN,Role.USER);
        Category category = new Category("TestCategory",null);
        Item item = new Item("TestMaker", "TestOrigin", "TestBrand",
                true, "TestBarcode");
        List<Images> links = new ArrayList<>();
        int price = 200;
        boolean orderMode = true;
        String rentalInfo = "TestRentalInfo";
        int clickCount = 5;
        int reviewCount = 10;
        int minimumPurchaseQuantity = 1;
        String optionDetail = "TestOptionDetail";
        Gender gender = Gender.MAN;
        int deliveryPrice = 10;
        String shippingSetting = "TestShippingSetting";
        boolean fastDelivery = true;
        boolean regularDelivery = true;
        boolean dawnDelivery = false;
        String isbn = "TestISBN";
        int stock = 20;

        return new ItemProduct(user, category, item, links, null,"TestItem", price, price,orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
    }

    public static ItemProduct getItemProductFixture(User user){
        Category category = new Category("TestCategory",null);
        Item item = new Item( "TestMaker", "TestOrigin", "TestBrand",
                true, "TestBarcode");
        List<Images> links = new ArrayList<>();
        int price = 200;
        boolean orderMode = true;
        String rentalInfo = "TestRentalInfo";
        int clickCount = 5;
        int reviewCount = 10;
        int minimumPurchaseQuantity = 1;
        String optionDetail = "TestOptionDetail";
        Gender gender = Gender.MAN;
        int deliveryPrice = 10;
        String shippingSetting = "TestShippingSetting";
        boolean fastDelivery = true;
        boolean regularDelivery = true;
        boolean dawnDelivery = false;
        String isbn = "TestISBN";
        int stock = 20;

        return new ItemProduct(user, category, item, links, null,"TestItem",price,price, orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
    }

    public static ItemProduct getItemProductFixture(User user, Category category, Item item){
        List<Images> links = new ArrayList<>();
        int price = 200;
        boolean orderMode = true;
        String rentalInfo = "TestRentalInfo";
        int clickCount = 5;
        int reviewCount = 10;
        int minimumPurchaseQuantity = 1;
        String optionDetail = "TestOptionDetail";
        Gender gender = Gender.MAN;
        int deliveryPrice = 10;
        String shippingSetting = "TestShippingSetting";
        boolean fastDelivery = true;
        boolean regularDelivery = true;
        boolean dawnDelivery = false;
        String isbn = "TestISBN";
        int stock = 20;

        return new ItemProduct(user, category, item, links, null,"TestItem",price,price, orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
    }
}
