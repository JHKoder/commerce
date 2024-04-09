package github.jhkoder.commerce.user.service.response;

import github.jhkoder.commerce.store.domain.Store;

public record SellerStoreResponse(String tradeName,int accountNumber,String bankName) {
    public static SellerStoreResponse of(Store store) {
        return new SellerStoreResponse(store.getTradeName(),store.getAccountNumber(), store.getBankName());
    }
}
