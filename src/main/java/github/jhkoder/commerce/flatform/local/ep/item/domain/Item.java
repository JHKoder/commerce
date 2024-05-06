package github.jhkoder.commerce.flatform.local.ep.item.domain;


import github.jhkoder.commerce.common.entity.BaseEntity;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.image.domain.Images;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "items")
@NoArgsConstructor(access = PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seqGen")
    @SequenceGenerator(name = "items_seqGen", sequenceName = "items_id_seq", initialValue = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "main_image_id")
    private Images mainImage;

    private String name;
    private int normalPrice;
    private String maker;
    private String origin;
    private String brand;
    private OracleBoolean brandCertification;
    private String barcode;

    private Item(Long id) {
        this.id = id;
    }

    public Item(Images mainImage, String name, int normalPrice, String maker, String origin, String brand, boolean brandCertification, String barcode) {
        this.mainImage = mainImage;
        this.name = name;
        this.normalPrice = normalPrice;
        this.maker = maker;
        this.origin = origin;
        this.brand = brand;
        this.brandCertification =OracleBoolean.of(brandCertification);
        this.barcode = barcode;
    }

    public Item(String name, int normalPrice, String maker, String origin, String brand, boolean brandCertification, String barcode) {
        this.name = name;
        this.normalPrice = normalPrice;
        this.maker = maker;
        this.origin = origin;
        this.brand = brand;
        this.brandCertification = OracleBoolean.of(brandCertification);
        this.barcode = barcode;
    }

    public static Item ofMeta(Long item) {
        return new Item(item);
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

    public void updateMainImage(Images mainImage) {
        this.mainImage=mainImage;
    }
}

