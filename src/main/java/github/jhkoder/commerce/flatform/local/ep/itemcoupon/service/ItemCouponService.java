package github.jhkoder.commerce.flatform.local.ep.itemcoupon.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.repository.ItemCouponDslRepository;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.repository.ItemCouponRepository;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.request.ItemCouponAddRequest;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.response.CouponQrCreateResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemCouponService {

    @Value("${qr.host}")
    private  String qrHost;
    private  String domain;
    private final WebClient webClient;
    private final ItemProductRepository productRepository;
    private final ItemCouponRepository couponRepository;
    private final ItemCouponDslRepository couponDslRepository;
    private final UserRepository userRepository;

    public CouponQrCreateResponse QRCodeCreate(ItemCouponAddRequest request) {
        String code = UUID.randomUUID().toString();
        String uri = "/coupon/qr/products/" + request.itemProductId() + "/access/"+code;
        String data= "http://"+domain+uri;

        int size = 150; // min = 10 , max = 1000 ,default: 200

        byte[] qrImage = webClient.get()
                .uri(qrHost + "/?data=" + data + "&size=" + size)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        return new CouponQrCreateResponse(code,qrImage);
    }

    public void validationQrRegistration() {
        if(countQrRegistration() >= 99_950 ){
            if(countQrRegistrationLimit() >= 99_999){
                throw new ApiException(ErrorCode.COUPON_QR_REGISTRATION_LIMIT);
            }
        }
    }

    public ItemCoupon couponCreate(ItemCouponAddRequest request) {
        ItemProduct product = productRepository.findById(request.itemProductId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));

        ItemCoupon coupon = new ItemCoupon(product,product.getCategory(),request.discountAmount(),request.startTime(),request.endTime());

        return couponRepository.save(coupon);
    }


    @Transactional
    public void qrImgUpdate(ItemCoupon coupon, Images image,String code) {
        coupon.updateImage(image);
        coupon.updateCode(code);
    }


    public void qrAccess(String username, Long productId, String code) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        ItemCoupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ApiException(ErrorCode.COUPON_CODE_NOT_FOUND));

        if(!coupon.getProduct().getId().equals(productId)){
            throw new ApiException(ErrorCode.COUPON_CODE_MISMATCH);
        }
    }

    private synchronized int countQrRegistrationLimit(){
        return couponDslRepository.countTodayCreatedCoupons();
    }

    private int countQrRegistration(){
        return couponDslRepository.countTodayCreatedCoupons();
    }

    public void codeAccess(String username, Long productId, String code) {
    }
}
