package github.jhkoder.commerce.user.service.request.signup;

import jakarta.validation.constraints.NotNull;

public record SignUpEmailSendRequest(
        @NotNull(message = "이메일을 입력해주세요.")
        String email) {
}
