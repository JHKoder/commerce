package github.jhkoder.commerce.user.service.response;

import github.jhkoder.commerce.store.domain.Store;

import java.util.Objects;

public record SellerStoreResponse(String tradeName,String accountNumber,String bankName) {

    public static SellerStoreResponse of(Store store) {
        return new SellerStoreResponse(store.getTradeName(),store.getAccountNumber(), store.getBankName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerStoreResponse response = (SellerStoreResponse) o;
        return Objects.equals(tradeName, response.tradeName) && Objects.equals(accountNumber, response.accountNumber) && Objects.equals(bankName, response.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeName, accountNumber, bankName);
    }
}
