package github.jhkoder.commerce.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.repository.dto.OrderUserAddressDto;
import github.jhkoder.commerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static github.jhkoder.commerce.order.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderDslRepository {
    private final JPAQueryFactory factory;

    public List<OrderUserAddressDto> findByUserLimit(User user, Long limit) {
        return factory
                .select(Projections.constructor(OrderUserAddressDto.class,
                        order.zipCode,
                        order.address,
                        order.detailedAddress,
                        order.referenceItems
                ))
                .from(order)
                .where(order.user.eq(user))
                .orderBy(order.id.desc())
                .limit(limit)
                .fetch();
    }
}
