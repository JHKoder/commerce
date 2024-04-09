package github.jhkoder.commerce.user.service.request.mypage;

import github.jhkoder.commerce.user.domain.User;

public record AddStoreRequest( String tradeName, int accountNumber, String bankName) {
}
