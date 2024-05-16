package github.jhkoder.commerce.order.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderUserAddressDto {

    private String zipCode;
    private String address;
    private String detailedAddress;
    private String referenceItems ;
}
