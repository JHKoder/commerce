package github.jhkoder.commerce.flatform.local.ep.category.service.response;

import lombok.Data;

import java.util.List;

@Data
public class CategoryAllResponse {
    private List<Category> category;

    @Data
    public static class Category {
        private Long categoryId;
        private String categoryName;
        private List<SubCategory> subCategories;
    }
    @Data
    public static class SubCategory {
        private Long categoryId;
        private String categoryName;
        private List<SubSubCategory> subSubCategories;

    }
    @Data
    public static class SubSubCategory {
        private Long categoryId;
        private String categoryName;
    }
}
