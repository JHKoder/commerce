package github.jhkoder.commerce.flatform.local.ep.category.repository.dto;

import lombok.ToString;

import java.util.List;

public class CategoryAddJsonDto {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public static class Category {
        private String main_category;
        private List<SubCategory> sub_categories;

        public String getMain_category() {
            return main_category;
        }

        public void setMain_category(String main_category) {
            this.main_category = main_category;
        }

        public List<SubCategory> getSub_categories() {
            return sub_categories;
        }

        public void setSub_categories(List<SubCategory> sub_categories) {
            this.sub_categories = sub_categories;
        }
    }

    public static class SubCategory {
        private String sub_category;
        private List<String> sub_sub_categories;

        public String getSub_category() {
            return sub_category;
        }

        public void setSub_category(String sub_category) {
            this.sub_category = sub_category;
        }

        public List<String> getSub_sub_categories() {
            return sub_sub_categories;
        }

        public void setSub_sub_categories(List<String> sub_sub_categories) {
            this.sub_sub_categories = sub_sub_categories;
        }
    }
}
