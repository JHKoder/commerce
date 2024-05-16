package github.jhkoder.commerce.order.service.response;

import github.jhkoder.commerce.order.repository.dto.OrderUserAddressDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderDeliveryListResponse {

    private String zipCode;
    private String address;
    private String detailedAddress;
    private String referenceItems ;

    public OrderDeliveryListResponse(String zipCode, String address, String detailedAddress, String referenceItems) {
        this.zipCode = zipCode;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.referenceItems = referenceItems;
    }

    public static List<OrderDeliveryListResponse> of(List<OrderUserAddressDto> list) {
        List<OrderDeliveryListResponse> responses = new ArrayList<>();
        for(var dto : list){
            responses.add(new OrderDeliveryListResponse(dto.getZipCode(),dto.getAddress(),dto.getDetailedAddress(),dto.getReferenceItems()));
        }
        return responses;
    }
}
