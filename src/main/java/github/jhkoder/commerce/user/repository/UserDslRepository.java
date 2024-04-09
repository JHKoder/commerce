package github.jhkoder.commerce.user.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import static github.jhkoder.commerce.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserDslRepository {

    private final JPAQueryFactory factory;

    public Page<User> findByPageUsers(PageRequest pageRequest, Role role) {
        OrderSpecifier<Long> orderSpecifier = new OrderSpecifier<>(Order.DESC, user.id);

        JPAQuery<User> query = factory
                .selectFrom(user)
                .where(user.role.eq(role))
                .orderBy(orderSpecifier)
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset());

        long totalCount = query.fetchCount();
        return new PageImpl<>(query.fetch(), pageRequest, totalCount);
    }
}
