package github.jhkoder.commerce.user.service.request.mypage;

import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.domain.UserAuthorizationApplication;

public record AdminRequestRoleListResponse(Long uaaId, Long userId, Role role, OracleBoolean isApprove) {

    public static AdminRequestRoleListResponse of(UserAuthorizationApplication uaa) {
        User user = uaa.getUser();

        return new AdminRequestRoleListResponse(uaa.getId(),user.getId(),user.getRole(),uaa.getIsApprove());
    }
}
