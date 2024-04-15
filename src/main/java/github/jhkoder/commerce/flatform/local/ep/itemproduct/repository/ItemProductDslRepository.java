package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.StoreSellerProductsPageResponse;
import github.jhkoder.commerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.QItemProduct.itemProduct;

@Repository
@RequiredArgsConstructor
public class ItemProductDslRepository {

    private final JPAQueryFactory factory;


    public Page<StoreSellerProductsPageResponse> findBySellerInStore(User user, Pageable page) {

        List<StoreSellerProductsPageResponse> result = factory
                .select(Projections.constructor(StoreSellerProductsPageResponse.class,
                                itemProduct.item.id,
                                itemProduct.item.name,
                                itemProduct.stock,
                                itemProduct.price
                        )
                )
                .from(itemProduct)
                .orderBy(itemProduct.createTime.desc())
                .limit(page.getPageSize())
                .offset(page.getOffset())
                .fetch();

        return new PageImpl<>(result, page, result.size());
    }
}
