package github.jhkoder.commerce.user.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 4, max = 20)

    @Column(name = "user_id", unique = true)
    private String userId;

    @Size(min = 2,max=30)
    @Column(name = "user_name")
    private String userName;
    private String password;

    @Column(name = "email", unique = true)
    private String email;
    @Column(unique = true)
    private String phone;


    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String userId, String userName, String password, String email, String phone, Gender gender, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.role = role;
    }

    public static User ofMeta(Long user) {
        return new User(user);
    }

    private User(Long id) {
        this.id = id;
    }

    public void roleUpdate(Role role){
        this.role=role;
    }

    public void updatePassword(String encodePassword) {
        this.password=encodePassword;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateEmail(String email) {
        this.email=email;
    }

    public void updateRole(Role role) {
        this.role = role;
    }
}
