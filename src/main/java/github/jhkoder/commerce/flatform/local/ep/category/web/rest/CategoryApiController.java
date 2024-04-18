package github.jhkoder.commerce.flatform.local.ep.category.web.rest;

import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryAddRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryChangeRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryUpdateRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
@RequiredArgsConstructor
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> find() {
        return categoryService.findAll();
    }

    @PostMapping
    public void add(@Valid @RequestBody CategoryAddRequest request) {
        categoryService.add(request);
    }

    @PatchMapping
    public void update(@Valid @RequestBody CategoryUpdateRequest request) {
        categoryService.update(request);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
    }

    @PatchMapping("/move")
    public void change(@Valid @RequestBody CategoryChangeRequest request){
        categoryService.move(request);
    }
}
