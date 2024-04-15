package github.jhkoder.commerce.image.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "images")
@NoArgsConstructor(access = PROTECTED)
public class Images extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String path;

    public Images(User user,String path) {
        this.user =user;
        this.path = path;
    }

    public void updatePath(String path) {
        this.path=path;
    }
}
