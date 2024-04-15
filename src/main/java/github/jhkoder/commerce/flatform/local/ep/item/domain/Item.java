package github.jhkoder.commerce.flatform.local.ep.item.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
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
    private int normalPrice;
    private String maker;
    private String origin;
    private String brand;
    private OracleBoolean brandCertification;
    private String barcode;

    public Item(String name, int normalPrice, String maker, String origin, String brand, boolean brandCertification, String barcode) {
        this.name = name;
        this.normalPrice = normalPrice;
        this.maker = maker;
        this.origin = origin;
        this.brand = brand;
        this.brandCertification = OracleBoolean.of(brandCertification);
        this.barcode = barcode;
    }

    public void updateAll(String name, int normalPrice, String maker, String origin, String brand, boolean brandCertification, String barcode){
        this.name = name;
        this.normalPrice = normalPrice;
        this.maker = maker;
        this.origin = origin;
        this.brand = brand;
        this.brandCertification = OracleBoolean.of(brandCertification);
        this.barcode = barcode;
    }
}

