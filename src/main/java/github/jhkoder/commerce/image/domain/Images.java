package github.jhkoder.commerce.image.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
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
    private String ppid;
    private String path;

    @Column(name = "file_size") // "size" 컬럼을 "file_size"로 변경
    private Long fileSize;

    @Enumerated(value = EnumType.STRING)
    private ImageExtension imageExtension;
}
