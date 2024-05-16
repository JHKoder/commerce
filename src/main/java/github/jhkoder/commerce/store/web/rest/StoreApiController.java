package github.jhkoder.commerce.store.web.rest;

import github.jhkoder.commerce.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/all/store")
@RequiredArgsConstructor
public class StoreApiController {

    private final StoreService storeService;

    // 특정 카테고리 페이징
    @GetMapping("/category/{categoryId}")
    public void category(){
    }

    // 상품 상세보기
    @GetMapping("/products/{roductId}")
    public void product(){

    }

}
