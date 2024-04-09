package github.jhkoder.commerce.user.service;

import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.domain.UserAuthorizationApplication;
import github.jhkoder.commerce.user.repository.UserAuthorizationApplicationRepository;
import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorizationApplicationService {
    private final UserAuthorizationApplicationRepository uaaRepository;
    private final UserRepository userRepository;

    // 권한 요청
    @Transactional
    public void roleRequest(User user, Role role) {
        UserAuthorizationApplication uaa = new UserAuthorizationApplication(user,user.getUserName(),role);
        uaaRepository.save(uaa);
    }

    // 권한 승인
    @Transactional
    public void roleAuthorization(String adminId, Long targetUserId) {
        User user = userRepository.findByUserId(adminId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if(!user.getRole().equals(Role.ADMIN)){
            throw new ApiException(ErrorCode.ADMIN_NOT);
        }

        UserAuthorizationApplication targetUser = uaaRepository.findById(targetUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        targetUser.update(Role.ADMIN);
        targetUser.getUser().roleUpdate(Role.ADMIN);
    }

    // 권한 요청리스트 보기
    @Transactional(readOnly = true)
    public List<UserAuthorizationApplication> roleList() {
        return uaaRepository.findByIsDeleted(OracleBoolean.F);
    }

}
