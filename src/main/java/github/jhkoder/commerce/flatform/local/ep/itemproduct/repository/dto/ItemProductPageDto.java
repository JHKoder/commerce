package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class ItemProductPageDto {
    private Long id;
    private String name;
    private int price;
    private int normalPrice;
    private int deliveryPrice;
}
