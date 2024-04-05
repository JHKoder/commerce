package github.jhkoder.commerce.user.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 4, max = 20)
    @Column(name = "user_id", unique = true)
    private String userId;
    @Column(name = "user_name")
    private String userName;
    private String password;

    @Size(min = 5, max = 255)
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

    @Getter
    public enum Gender {
        MAN("남성"),
        WOMAN("여성");
        private final String gender;

        Gender(String gender) {
            this.gender = gender;
        }
    }
}
