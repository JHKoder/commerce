package fixture;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;

public class CategoryFixture {
    public static Category getCategoryFixture(){
        return new Category("top category", CategoryLevel.TOP);
    }
}
