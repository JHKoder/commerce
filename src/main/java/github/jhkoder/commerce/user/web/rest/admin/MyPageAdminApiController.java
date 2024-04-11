package github.jhkoder.commerce.user.web.rest.admin;


import github.jhkoder.commerce.user.service.AdminService;
import github.jhkoder.commerce.user.service.request.mypage.AdminDashBoardRequest;
import github.jhkoder.commerce.user.service.request.mypage.AdminMemberRoleRequest;
import github.jhkoder.commerce.user.service.request.mypage.AdminRequestRoleListResponse;
import github.jhkoder.commerce.user.service.request.mypage.AdminUsersDashBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/mypage")
@RequiredArgsConstructor
public class MyPageAdminApiController {

    private final AdminService adminService;

    @GetMapping("/users")
    public AdminUsersDashBoardResponse admin_users_status_view(@RequestBody AdminDashBoardRequest  request){
        return adminService.allUserList(request.page());
    }

    @GetMapping("/roles")
    public List<AdminRequestRoleListResponse> admin_member_role_list(){
        // admin 권한 요청 리스트 보기
        return adminService.memberRoleRequestList();
    }

    @PatchMapping("/role")
    public void admin_member_role_accept(@RequestBody AdminMemberRoleRequest request){
        //타겟 권한 요청 승인 & 거절
        adminService.memberRoleUpdate(request.targetUserId(), request.ok());
    }
}
