package github.jhkoder.commerce.flatform.local.ep.category.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import github.jhkoder.commerce.flatform.local.ep.category.repository.dto.CategoryAddJsonDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryMetadataRepository {
    private final CategoryRepository categoryRepository;

    @Transactional
//    @PostConstruct
    public void initUpdateCategoryPath() {
        categoryRepository.findByLevel(CategoryLevel.TOP)
                .ifPresent(list -> list.forEach(category -> childUpdate(category, "")));
    }

    private void childUpdate(Category category, String path) {
        String categoryPath = pathUpdate(category, path);
        categoryRepository.findByParent(category).forEach(child -> childUpdate(child, categoryPath));
        categoryRepository.save(category);
    }

    private String pathUpdate(Category category, String path) {
        String categoryPath = path + category.getId() + "/";
        category.updatePath(categoryPath);
        return categoryPath;
    }


    @Transactional
    public void saveCategoriesFromJsonFile() {
        try {
            ClassPathResource resource = new ClassPathResource("/sampleData/category.json");
            ObjectMapper objectMapper = new ObjectMapper();
            CategoryAddJsonDto jsonDto = objectMapper.readValue(resource.getInputStream(), CategoryAddJsonDto.class);
            log.info("[Init] ===== category dates init task ");
            List<CategoryAddJsonDto.Category> categories = jsonDto.getCategories();
            for (CategoryAddJsonDto.Category category : categories) {
                saveCategory(category);
            }
        } catch (IOException ignored) {
            log.error("category dates save Failed");
        }
        log.info("category dates all save.. ");
    }


    private void saveCategory(CategoryAddJsonDto.Category categoryDto) {
        Category category = categoryRepository.save(new Category(categoryDto.getMain_category(), CategoryLevel.TOP));

        for (var subCategoryDto : categoryDto.getSub_categories()) {
            Category subCategory = categoryRepository.save(new Category(subCategoryDto.getSub_category(), CategoryLevel.MID, category));
            childSave(subCategory, subCategoryDto.getSub_sub_categories());
        }
    }

    private void childSave(Category topCategory, List<String> categories) {
        if (topCategory.getChild() != null || categories.isEmpty()) {
            return;
        }

        for (String categoryName : categories) {
            categoryRepository.save(new Category(categoryName, CategoryLevel.BOTTOM, topCategory));
        }
    }
}
