package github.jhkoder.commerce.user.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.domain.UserAuthorizationApplication;
import github.jhkoder.commerce.user.repository.UserAuthorizationApplicationRepository;
import github.jhkoder.commerce.user.repository.UserDslRepository;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.request.mypage.AdminRequestRoleListResponse;
import github.jhkoder.commerce.user.service.request.mypage.AdminUsersDashBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserDslRepository dslRepository;
    private final UserAuthorizationApplicationRepository uaaRepository;
    public AdminUsersDashBoardResponse allUserList(int pageNumber ) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<User> users = dslRepository.findByPageUsers(pageRequest,Role.USER);
        int total = users.getTotalPages();

        List<AdminUsersDashBoardResponse.Users> user = dslRepository
                .findByPageUsers(pageRequest,Role.SELLER)
                .getContent()
                .stream()
                .map(AdminUsersDashBoardResponse.Users::of)
                .toList();

        List<AdminUsersDashBoardResponse.Users> seller = dslRepository
                .findByPageUsers(pageRequest,Role.SELLER)
                .getContent()
                .stream()
                .map(AdminUsersDashBoardResponse.Users::of)
                .toList();

        List<AdminUsersDashBoardResponse.Users>  admins =  dslRepository
                .findByPageUsers(pageRequest,Role.ADMIN)
                .getContent()
                .stream()
                .map(AdminUsersDashBoardResponse.Users::of)
                .toList();


        return new AdminUsersDashBoardResponse(user,seller,admins,total,pageNumber);

    }

    public List<AdminRequestRoleListResponse> memberRoleRequestList() {
        return uaaRepository.findAll()
                .stream()
                .map(AdminRequestRoleListResponse::of)
                .toList();
    }

    @Transactional
    public void memberRoleUpdate(Long targetUserId, boolean is) {
        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        UserAuthorizationApplication uaa =  uaaRepository.findByUser(user)
                        .orElseThrow(() -> new ApiException(ErrorCode.ADMIN_TARGET_USER_NO_PERMISSION_REQUEST));
        user.updateRole(uaa.getRole());
    }
}
