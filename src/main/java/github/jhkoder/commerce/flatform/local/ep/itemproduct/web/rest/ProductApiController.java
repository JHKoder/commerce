package github.jhkoder.commerce.flatform.local.ep.itemproduct.web.rest;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.ProductService;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.request.ProductCategoryPageRequest;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductsCategoryPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/all/products")
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductService productService;

    // 특정 카테고리 페이징
    @GetMapping("/category")
    public Page<ProductsCategoryPageResponse> category(@Valid @RequestBody ProductCategoryPageRequest request){
        return productService.findCategoryPage(request) ;
    }

    // 상품 상세보기
    @GetMapping("/products/{productId}")
    public ProductResponse product(@PathVariable Long productId){
        return productService.findProduct(productId);
    }

}
