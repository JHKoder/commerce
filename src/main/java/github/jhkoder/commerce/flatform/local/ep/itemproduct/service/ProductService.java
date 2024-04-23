package github.jhkoder.commerce.flatform.local.ep.itemproduct.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductDslRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.request.ProductCategoryPageRequest;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductsCategoryPageResponse;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.repository.ImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ItemProductDslRepository productDslRepository;
    private final ItemProductRepository productRepository;
    private final ImageJpaRepository imageRepository;

    public Page<ProductsCategoryPageResponse> findCategoryPage(ProductCategoryPageRequest request) {

        Pageable pageable = PageRequest.of(request.page(), request.pageSize());
        // category
        return productDslRepository.findbyCategoryPage(request.categoryId(), pageable);
    }


    @Transactional(readOnly = true)
    public ProductResponse findProduct(Long productId) {
        ItemProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        Item item = product.getItem();
        Optional<Images> images = imageRepository.findById(item.getMainImage().getId());

        String mainImageLink = images.isPresent() ? images.get().getPath(): "img/notImg.png";

        return ProductResponse.of(product,
                item,
                mainImageLink);
    }
}
