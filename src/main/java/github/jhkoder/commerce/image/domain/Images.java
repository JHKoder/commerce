package github.jhkoder.commerce.image.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "images", indexes = {@Index(name = "idx_image_path", columnList = "path")})
@NoArgsConstructor(access = PROTECTED)
public class Images extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_seqGen")
    @SequenceGenerator(name = "images_seqGen", sequenceName = "images_id_seq", initialValue = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // @Column 대신 @JoinColumn 사용
    private User user;

    private String path;

    public Images(User user,String path) {
        this.user =user;
        this.path = path;
    }


    private Images(Long id) {
        this.id = id;
    }

    public static Images of(Long id){
        return new Images(id);
    }

    public static List<Images> ofMeta(List<Long> links) {
        return links.stream()
                .map(Images::new)
                .toList();
    }

    public static Images ofMeta(Long mainImage) {
        return new Images(mainImage);
    }

    public void updatePath(String path) {
        this.path=path;
    }

    public Images(String path) {
        this.path = path;
    }

    public void updateId(Long id) {
        this.id=id;
    }
}
