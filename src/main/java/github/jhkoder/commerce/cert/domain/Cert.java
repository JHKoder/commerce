package github.jhkoder.commerce.cert.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Cert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    private int verificationCode;
    private String verificationSent;

    @Enumerated(value = EnumType.STRING)
    private CertAuthentication certAuthentication;
    @Enumerated(value = EnumType.STRING)
    private OracleBoolean authentication;

    public Cert(int verificationCode, String verificationSent, CertAuthentication certAuthentication, OracleBoolean authentication) {
        this.verificationCode = verificationCode;
        this.verificationSent = verificationSent;
        this.certAuthentication = certAuthentication;
        this.authentication=authentication;
    }
}
