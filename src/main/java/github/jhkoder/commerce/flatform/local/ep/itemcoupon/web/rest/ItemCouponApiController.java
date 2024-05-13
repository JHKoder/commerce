package github.jhkoder.commerce.flatform.local.ep.itemcoupon.web.rest;

import github.jhkoder.commerce.flatform.local.ep.itemcoupon.domain.ItemCoupon;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.ItemCouponService;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.request.ItemCouponAddRequest;
import github.jhkoder.commerce.flatform.local.ep.itemcoupon.service.response.CouponQrCreateResponse;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.service.ImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class ItemCouponApiController {
    private final ImageService imageService;
    private final ItemCouponService couponService;

//    내부적 기능 대폭 수정 예정
//    @GetMapping("/qr/products/{productId}/access/{code}")
//    public void qrAccess(@PathVariable Long productId, @PathVariable String code,@AuthenticationPrincipal UserDetails userDetails){
//        couponService.qrAccess(userDetails.getUsername(),productId,code);
//        // 당첨된 쿠폰
//    }

    @PostMapping("/qr/create")
    public void qrAdd( @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody ItemCouponAddRequest request){
        couponService.validationQrRegistration();
        ItemCoupon coupon = couponService.couponCreate(request);
        CouponQrCreateResponse qrImage = couponService.QRCodeCreate(request);
        Images image = imageService.upload(userDetails.getUsername(),"item_"+request.itemId(), new ByteArrayInputStream(qrImage.img()));
        couponService.qrImgUpdate(coupon,image,qrImage.code());
    }
}
