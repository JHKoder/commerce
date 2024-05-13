package github.jhkoder.commerce.flatform.local.itemevent.service;

import fixture.CategoryFixture;
import fixture.UserFixture;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import github.jhkoder.commerce.flatform.local.ep.itemevent.domain.ItemEvent;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static fixture.ItemCouponFixture.getItemCouponFixture;
import static fixture.ItemEventFixture.getItemEventFixture;
import static fixture.ItemFixture.getItemFixture;
import static fixture.ItemProductFixture.getItemProductFixture;

@DisplayName("이벤트")
public class ItemEventServiceTest {

    @Test
    void 등록(){
        User user = UserFixture.getFixtureUser();
        Category category = CategoryFixture.getCategoryFixture();
        Item item = getItemFixture();
        ItemProduct itemProduct = getItemProductFixture(user,category,item);

        int amount = 10_000; // 가격 인하
        String work = " 할인 문구 ";
        String imgLink = " 이미지 링크 ";
        //img 가능한 타입 높이 최대 250px , 가로 최대 1000px,
        // [1000, 250, 400, 500, 750]
        //
        ItemEvent event = new ItemEvent(itemProduct,category,0,"",false, LocalDateTime.now(),LocalDateTime.now().plusDays(1));


        ItemCoupon coupon = getItemCouponFixture(itemProduct,item,category);
        // 쿠폰 생성
            /*
            판매자 구폰생성 , 쿠폰 유형, 할인금액, 비율, 사용가능 기간
             */
        // 쿠폰 발급
            /*
                QR 등록후 img
             */
        // 쿠폰 활성화

        // 쿠폰 적용
        // 쿠폰 사용



    }


    @Test
    void 조회(){

    }


    @Test
    void 수정(){

    }


    @Test
    void 삭제(){

    }
}
