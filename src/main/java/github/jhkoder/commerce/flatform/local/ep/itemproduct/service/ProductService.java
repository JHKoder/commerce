package github.jhkoder.commerce.flatform.local.ep.itemproduct.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductDslRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.dto.CategoryChildDto;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ItemProductDslRepository productDslRepository;
    private final ItemProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductsCategoryPageResponse> findCategoryPage(String categoryPath , ProductCategoryPageRequest request) {

        Pageable pageable = PageRequest.of(0, request.pageSize());
        return productDslRepository.findByCategoryPage(categoryPath, request.lastProductId(), pageable);
    }


    @Transactional(readOnly = true)
    public ProductResponse findProduct(Long productId) {
        ItemProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        String mainImageLink = product.getMainImage() != null ? product.getMainImage().getPath() : "img/notImg.png";

        List<String> imagesContexts = product.getLinks().stream()
                .map(Images::getPath)
                .toList();

        List<String> categoryPath = Arrays.stream(product.getCategory().getPath().split("/"))
                .map(Long::parseLong)
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Category::getName)
                .toList();

        return ProductResponse.of(product, product.getItem(), mainImageLink, imagesContexts, categoryPath);
    }
}
