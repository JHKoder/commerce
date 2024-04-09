package github.jhkoder.commerce.flatform.local.ep.item.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "local_item")
@NoArgsConstructor(access = PROTECTED)
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String barcode;
    private String brand;
    private String maker;
    private String origin;
}

