package github.jhkoder.commerce.store.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.store.domain.Store;
import github.jhkoder.commerce.store.repository.StoreRepository;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.request.mypage.AddStoreRequest;
import github.jhkoder.commerce.user.service.request.mypage.UpdateStoreRequest;
import github.jhkoder.commerce.user.service.response.SellerStoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SellerStoreResponse findByUserId(String username) {
        Store store = findUserNameStore(username);
        return SellerStoreResponse.of(store);
    }

    @Transactional
    public SellerStoreResponse addStore(String username, AddStoreRequest request) {
        User user = findUser(username);

        Store store = new Store(user, request.tradeName(), request.accountNumber(), request.bankName());

        return SellerStoreResponse.of(store);
    }

    @Transactional
    public SellerStoreResponse update(String username, UpdateStoreRequest request) {
        Store store = findUserNameStore(username);
        store.update(request.tradeName(),request.accountNumber(),request.bankName());
        return SellerStoreResponse.of(store);
    }

    @Transactional
    public void delete(String username) {
        storeRepository.delete(findUserNameStore(username));

    }

    private User findUser(String username) {
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }

    private Store findUserNameStore(String username) {
        User user = findUser(username);
        return storeRepository.findByUser(user);
    }
}
