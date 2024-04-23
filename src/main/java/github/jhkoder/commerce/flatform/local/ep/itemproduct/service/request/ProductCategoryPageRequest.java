package github.jhkoder.commerce.flatform.local.ep.itemproduct.service.request;

import jakarta.validation.constraints.Max;

public record ProductCategoryPageRequest(
        Long categoryId,
        int page,
        @Max(value = 100,message = "페이지 사이트 최대 100 초과 ")
        int pageSize

) {
}
