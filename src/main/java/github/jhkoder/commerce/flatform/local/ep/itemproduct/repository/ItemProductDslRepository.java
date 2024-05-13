package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.flatform.local.ep.category.domain.QCategory;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.QItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.ItemProductPageDto;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.StoreSellerProductsPageResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductsCategoryPageResponse;
import github.jhkoder.commerce.image.domain.QImages;
import github.jhkoder.commerce.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
                                itemProduct.item.maker,
                                itemProduct.stock,
                                itemProduct.price
                        )
                )
                .from(itemProduct)
                .where(itemProduct.user.eq(user))
                .orderBy(itemProduct.createTime.desc())
                .limit(page.getPageSize())
                .offset(page.getOffset())
                .fetch();

        return new PageImpl<>(result, page, result.size());
    }

    public Page<ProductsCategoryPageResponse> findByCategoryPage(String categoryPath, int lastCategoryId, Pageable page) {
        QItemProduct itemProduct = QItemProduct.itemProduct;
        QCategory category = QCategory.category;
        QImages images = QImages.images;

        List<ItemProductPageDto> itemProducts = factory
                .select(toProductDto(itemProduct))
                .from(itemProduct)
                .leftJoin(itemProduct.category, category)
                .where(itemProduct.id.gt(lastCategoryId)
                        .and(category.path.startsWith(categoryPath)))
                .limit(page.getPageSize())
                .fetch();

        List<String> imgs = factory
                .select(images.path)
                .from(images)
                .where(images.id.in(
                                factory
                                        .select(itemProduct.mainImage.id)
                                        .from(itemProduct)
                                        .leftJoin(itemProduct.category, category)
                                        .where(itemProduct.id.gt(lastCategoryId)
                                                .and(category.path.startsWith(categoryPath)))
                                        .limit(page.getPageSize())
                                        .fetch()
                        )
                )
                .fetch();

        List<ProductsCategoryPageResponse> responses = new ArrayList<>();
        for(int i = 0; i < itemProducts.size(); i++){
            ItemProductPageDto product = itemProducts.get(i);
            responses.add(new ProductsCategoryPageResponse(product.getId(), product.getName(), imgs.get(i), product.getPrice(), product.getPrice(), product.getDeliveryPrice()));
        }
        return new PageImpl<>(responses, page, responses.size());
    }

    private ConstructorExpression<ItemProductPageDto> toProductDto(QItemProduct itemProduct) {
        return Projections.constructor(ItemProductPageDto.class,
                itemProduct.id,
                itemProduct.name,
                itemProduct.price,
                itemProduct.normalPrice,
                itemProduct.deliveryPrice
        );
    }
}
