package github.jhkoder.commerce.store.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "stores")
@NoArgsConstructor(access = PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String tradeName;
    private String accountNumber;
    private String bankName;

    public Store(User user, String tradeName, String accountNumber, String bankName) {
        this.user = user;
        this.tradeName = tradeName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    public void update(String tradeName, String accountNumber, String bankName) {
        this.tradeName = tradeName;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
}
