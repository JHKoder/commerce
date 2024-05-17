package github.jhkoder.commerce.order.service.request;

import github.jhkoder.commerce.order.domain.Order;
import github.jhkoder.commerce.order.domain.OrderStatus;
import github.jhkoder.commerce.payment.domain.Payment;

public record OrderPayRequest(
        Long productId,
        Long orderId,
        int totalPrice,
        int discountAmount,
        int vat,
        int installmentPeriod,
        int supplyAmount,
        int exemptAmount,
        int actualPaymentAmount,
        boolean discounted,
        String receiptId,
        String pg,
        String paymentMethod,
        String pgTid,
        String cardCompany,
        String cardNumber,
        String approvalNumber,
        String cardType,
        String corporateOrIndividual,
        String zipCode,
        String address,
        String detailedAddress,
        String referenceItems
) {


    public Payment ofPayment(Order order){
        return new Payment(
                order, OrderStatus.STATUS_0_NUMBER,
                discountAmount(),
                vat(),
                actualPaymentAmount(),
                discounted(),
                pg(),
                paymentMethod(),
                pgTid(),
                cardCompany(),
                cardNumber(),
                approvalNumber(),
                cardType(),
                corporateOrIndividual(),
                zipCode(),
                address(),
                detailedAddress(),
                referenceItems()
        );

    }
}
