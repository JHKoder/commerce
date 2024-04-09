package github.jhkoder.commerce.user.service.request.mypage;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;

import java.util.List;

public record AdminUsersDashBoardResponse(List<Users> users, List<Users> seller, List<Users> admins, int total,int pageNumber) {
    public record Users(String username, String email, String phone, Gender gender, Role role  ){

        public static Users of(User user){
            return new Users(user.getUserName(),user.getEmail(),user.getPhone(),user.getGender(),user.getRole());
        }
    }
}
