package github.jhkoder.commerce.flatform.local.ep.category.service.response;

import java.util.List;


public record CategoryResponse(String name, Long categoryId, List<MiddleCategory> middleCategories) {
    public record MiddleCategory(String name, Long categoryId, List<SmallCategory> smallCategories) {
    }
    public record SmallCategory(String name, Long categoryId) {
    }
}
