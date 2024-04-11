package github.jhkoder.commerce.user.web.rest.seller;

import github.jhkoder.commerce.store.service.StoreService;
import github.jhkoder.commerce.user.service.request.mypage.AddStoreRequest;
import github.jhkoder.commerce.user.service.request.mypage.UpdateStoreRequest;
import github.jhkoder.commerce.user.service.response.SellerStoreResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('SELLER')")
@RequestMapping("/api/seller/mypage")
@RequiredArgsConstructor
public class MyPageSellerApiController {

    private final StoreService storeService;

    @GetMapping("/store")
    public SellerStoreResponse getStore(@AuthenticationPrincipal UserDetails userDetails){
        return storeService.findByUserId(userDetails.getUsername());
    }

    @PostMapping("/store")
    public SellerStoreResponse saveStore(@AuthenticationPrincipal UserDetails userDetails,
                                         @Valid @RequestBody AddStoreRequest request){
        return storeService.addStore(userDetails.getUsername(),request);

    }

    @PatchMapping("/store")
    public SellerStoreResponse updateStore(@AuthenticationPrincipal UserDetails userDetails,
                            @Valid @RequestBody UpdateStoreRequest request){
        return storeService.update(userDetails.getUsername(),request);
    }

    @DeleteMapping("/store")
    public void deleteStore(@AuthenticationPrincipal UserDetails userDetails){
        storeService.delete(userDetails.getUsername());
    }
}
