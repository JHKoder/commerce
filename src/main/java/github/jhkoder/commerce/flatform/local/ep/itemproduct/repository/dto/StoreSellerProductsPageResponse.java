package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class StoreSellerProductsPageResponse {
    private Long itemId;
    private String name;
    private int stock;
    private int price;


    @QueryProjection
    public StoreSellerProductsPageResponse(Long itemId, String name, int stock,int price) {
        this.itemId = itemId;
        this.name = name;
        this.stock = stock;
        this.price=price;
    }
}

/*
@Data
public class StoreSellerProductsPageResponse {
    private Long itemId;
    private String name;
    private int stock;
    private int price;


    @QueryProjection
    public StoreSellerProductsPageResponse(Long itemId, String name, int stock,int price) {
        this.itemId = itemId;
        this.name = name;
        this.stock = stock;
        this.price=price;
    }
}

 */