package github.jhkoder.commerce.flatform.local.ep.category.web.rest;

import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryAddRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryUpdateRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping
    public CategoryResponse find() {
        return categoryService.findAll();
    }

    @PostMapping
    public void add(@Valid @RequestBody CategoryAddRequest request) {
        categoryService.add(request);
    }

    @PatchMapping("/{categoryId}")
    public void update(@Valid @RequestBody CategoryUpdateRequest request) {
        categoryService.update(request);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }

}
