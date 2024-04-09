package github.jhkoder.commerce.user.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "user_authorization_application")
@NoArgsConstructor(access = PROTECTED)
public class UserAuthorizationApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String userName;
    private Role role;
    private OracleBoolean isApprove;
    private OracleBoolean isDeleted;

    public UserAuthorizationApplication(User user,String userName, Role role) {
        this.user = user;
        this.userName=userName;
        this.isApprove = OracleBoolean.F;
        this.isDeleted = OracleBoolean.F;
    }

    public void update(Role role){
        this.user.roleUpdate(role);
        this.isApprove = OracleBoolean.F;
        this.isDeleted = OracleBoolean.T;
    }
}
