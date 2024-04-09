package github.jhkoder.commerce.user.service.response;

import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;

import java.time.LocalDateTime;

public record MyPageUserResponse(String name, String email, String phone, Role role, LocalDateTime createTime) {

    public static MyPageUserResponse of(User user){
        return new MyPageUserResponse(user.getUserName(),user.getEmail(),user.getPhone(),user.getRole(),user.getCreateTime());
    }

}
