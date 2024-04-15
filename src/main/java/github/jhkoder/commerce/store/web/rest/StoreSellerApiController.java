package github.jhkoder.commerce.store.web.rest;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.StoreSellerProductsPageResponse;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.service.ImageService;
import github.jhkoder.commerce.store.service.StoreSellerService;
import github.jhkoder.commerce.store.service.request.StoreAddProductRequest;
import github.jhkoder.commerce.store.service.request.StoreUpdateProductRequest;
import github.jhkoder.commerce.store.service.response.StoreSellerProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/store")
@RequiredArgsConstructor
public class StoreSellerApiController {
    private final StoreSellerService storeService;
    private final ImageService imageService;

    @PostMapping("/product")
    public void addProduct(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid @RequestBody StoreAddProductRequest request) {
        ItemProduct itemProduct = storeService.add(userDetails.getUsername(), request);

        List<Images> imagePaths = imageService.upload(itemProduct, request.images());
        storeService.updateProductImages(itemProduct, imagePaths);
    }

    @GetMapping("/products/page/{page}")
    public Page<StoreSellerProductsPageResponse> findProducts(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable int page) {
        return storeService.findPageProducts(userDetails.getUsername(), page);
    }

    @GetMapping("/products/{productId}")
    public StoreSellerProductResponse findProduct(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable Long productId) {
        return storeService.findProduct(userDetails.getUsername(), productId);
    }

    @PatchMapping("/products/{productId}")
    public void updateProduct(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @RequestBody StoreUpdateProductRequest request,
                              @PathVariable Long productId) {
        storeService.updateProduct(userDetails.getUsername(), request, productId);
        imageService.change(userDetails.getUsername(), request.file());
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long productId) {
        List<Images> images = storeService.deleteProduct(userDetails.getUsername(), productId);
        imageService.delete(images);
    }
}
