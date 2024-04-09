package github.jhkoder.commerce.user.service.request.signup;

import jakarta.validation.constraints.NotNull;

public record SignUpSmsSendRequest(
        @NotNull(message = "휴대폰을 입력해주세요.")
        String sms) {
}
