package github.jhkoder.commerce.flatform.local.ep.itemcoupon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.QItemCoupon.itemCoupon;

@Repository
@RequiredArgsConstructor
public class ItemCouponDslRepository {

    private final JPAQueryFactory factory;

    public int countTodayCreatedCoupons() {
        int day = 1;
        LocalDate today = LocalDate.now();
        return factory.selectFrom(itemCoupon)
                .where(itemCoupon.createTime.between(today.minusDays(day).atStartOfDay(), today.atStartOfDay()))
                .fetch()
                .size();
    }
}
