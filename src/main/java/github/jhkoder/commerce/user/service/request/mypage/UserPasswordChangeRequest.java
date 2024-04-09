package github.jhkoder.commerce.user.service.request.mypage;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserPasswordChangeRequest(

        @Pattern(regexp = "^[a-zA-Z0-9]{8,16}$", message = "비밀번호: 8~16자의 영문,숫자를 사용해주세요.")
        @NotNull(message = "비밀번호: 필수 정보입니다.")
        String password) {
}
