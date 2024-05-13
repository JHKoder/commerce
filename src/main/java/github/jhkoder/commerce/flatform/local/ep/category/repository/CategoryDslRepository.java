package github.jhkoder.commerce.flatform.local.ep.category.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.flatform.local.ep.category.domain.QCategory;
import github.jhkoder.commerce.flatform.local.ep.category.repository.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel.*;

@Repository
@RequiredArgsConstructor
public class CategoryDslRepository {


    private final JPAQueryFactory factory;

    public List<CategoryDto> getCategories() {

        QCategory top = new QCategory("top");
        QCategory mid = new QCategory("mid");
        QCategory bottom = new QCategory("bottom");
        return factory
                .select(
                        Projections.constructor(
                                CategoryDto.class,
                                top.id.as("topCategoryId"),
                                top.name.as("topCategoryName"),
                                mid.id.as("midSubcategoryId"),
                                mid.name.as("midSubcategoryName"),
                                bottom.id.as("bottomCategoryId"),
                                bottom.name.as("bottomCategoryName")
                        )
                )
                .from(top)
                .leftJoin(mid).on(top.id.eq(mid.parent.id))
                .leftJoin(bottom).on(mid.id.eq(bottom.parent.id))
                .where(
                        top.level.eq(TOP),
                        mid.level.eq(MID),
                        bottom.level.eq(BOTTOM)
                )
                .orderBy(
                        top.id.asc(),
                        mid.id.asc(),
                        bottom.id.asc()
                )
                .fetch();
    }
}
