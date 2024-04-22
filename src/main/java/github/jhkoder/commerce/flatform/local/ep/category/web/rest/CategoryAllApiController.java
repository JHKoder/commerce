package github.jhkoder.commerce.flatform.local.ep.category.web.rest;

import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryTopResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/all/category")
@RequiredArgsConstructor
public class CategoryAllApiController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/top")
    public List<CategoryTopResponse> findTop(){
        return categoryService.findTopCategory();
    }
}
