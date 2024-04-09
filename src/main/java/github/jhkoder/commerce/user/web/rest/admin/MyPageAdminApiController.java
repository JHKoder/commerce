package github.jhkoder.commerce.user.web.rest.admin;


import github.jhkoder.commerce.user.service.AdminService;
import github.jhkoder.commerce.user.service.request.mypage.AdminRequestRoleListResponse;
import github.jhkoder.commerce.user.service.request.mypage.AdminUsersDashBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/admin/mypage")
@RequiredArgsConstructor
public class MyPageAdminApiController {

    private final AdminService adminService;

    @GetMapping("/users")
    public AdminUsersDashBoardResponse admin_users_status_view(int pagenumber){
        return adminService.allUserList(pagenumber);
    }

    @GetMapping("/roles")
    public List<AdminRequestRoleListResponse> admin_member_role_list(){
        // admin 권한 요청 리스트 보기
        return adminService.memberRoleRequestList();
    }

    @PatchMapping("/role")
    public void admin_member_role_accept(Long targetUserId){
        //타겟 권한 요청 승인
        adminService.memberRoleUpdate(targetUserId, true);
    }

    @DeleteMapping("/role")
    public void admin_member_role_reject(Long targetUserId){
        //타겟 권한 요청 거절
        adminService.memberRoleUpdate(targetUserId, false);

    }
}
