package github.jhkoder.commerce.store.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.item.repository.ItemRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductDslRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.StoreSellerProductsPageResponse;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.store.service.request.StoreAddProductRequest;
import github.jhkoder.commerce.store.service.request.StoreUpdateProductRequest;
import github.jhkoder.commerce.store.service.response.StoreSellerProductResponse;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSellerService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemProductRepository productRepository;
    private final ItemProductDslRepository productDslRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ItemProduct add(String username, StoreAddProductRequest request) {
        Category category = categoryRepository.findById(request.product().categoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));

        User user = findByUser(username);
        Item item = itemRepository.save(request.item().toItem());

        return productRepository.save(request.product().toProduct(user, category, item));
    }

    @Transactional
    public void updateProductImages(ItemProduct itemProduct, List<Images> imagePaths) {
        itemProduct.updateImages(imagePaths);
        productRepository.save(itemProduct);
    }

    //물품 리스트 조회 페이징
    @Transactional(readOnly = true)
    public Page<StoreSellerProductsPageResponse> findPageProducts(String username, int page) {
        User user = findByUser(username);
        Pageable pageable = PageRequest.of(page, 10);
        return productDslRepository.findBySellerInStore(user, pageable);
    }

    //지정 물품 조회
    @Transactional(readOnly = true)
    public StoreSellerProductResponse findProduct(String username, Long productId) {
        ItemProduct product =  productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getUser().getUserId().equalsIgnoreCase(username)){
            throw new ApiException(ErrorCode.PRODUCT_NOT_SELLER_USER);
        }

        return  StoreSellerProductResponse.of(product);
    }

    //지정 물품 수정
    @Transactional
    public void updateProduct(String username, StoreUpdateProductRequest request, Long productId) {
        ItemProduct itemProduct = validItemProduct(username,productId);
        var product= request.product();
        var reqItem =request.item();
        itemProduct.updateAll(product.price(),
                product.orderMode(),
                product.rentalInfo(),
                product.clickCount(),
                product.reviewCount(),
                product.minimumPurchaseQuantity(),
                product.optionDetail(),
                product.gender(),
                product.deliveryPrice(),
                product.shippingSetting(),
                product.fastDelivery(),
                product.regularDelivery(),
                product.dawnDelivery(),
                product.isbn(),
                product.stock());
        itemProduct.getItem().updateAll(reqItem.name(),
                reqItem.normalPrice(),
                reqItem.maker(),
                reqItem.origin(),
                reqItem.brand(),
                reqItem.brandCertification(),
                reqItem.barcode());
    }

    //지정 물품 삭제
    public List<Images> deleteProduct(String username, Long productId) {
        ItemProduct product =validItemProduct(username,productId);
        Item item = product.getItem();
        productRepository.delete(product);
        itemRepository.delete(item);
        return product.getLinks();
    }

    private User findByUser(String username) {
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private ItemProduct validItemProduct(String username,Long productId){

        ItemProduct product =productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        if(!product.getUser().getUserId().equalsIgnoreCase(username)){
            throw new ApiException(ErrorCode.PRODUCT_NOT_SELLER_USER);
        }
        return product;
    }
}
