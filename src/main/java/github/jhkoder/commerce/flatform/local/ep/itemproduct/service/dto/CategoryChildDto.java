package github.jhkoder.commerce.flatform.local.ep.itemproduct.service.dto;

import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

public record CategoryChildDto(
        long rootId,
        String rootName,
        List<CategoryChildDto> childCategory
) {
    public static CategoryChildDto from(Category category) {
        return new CategoryChildDto(category.getId(), category.getName(), toDto(category.getChild()));
    }

    private static List<CategoryChildDto> toDto(List<Category> categories) {
        if (categories.isEmpty()) {
            return List.of();
        }
        return categories.stream()
                .map(category -> new CategoryChildDto(category.getId(), category.getName(), toDto(category.getChild())))
                .toList();
    }

    public String path(){
        return "" +rootId + childPath();
    }

    private String childPath() {
        return childCategory.stream().map(child ->String.valueOf(child.rootId)).collect(Collectors.joining());
    }
}
