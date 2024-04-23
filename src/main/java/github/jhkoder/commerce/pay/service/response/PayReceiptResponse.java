package github.jhkoder.commerce.pay.service.response;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public record PayReceiptResponse(
        String receiptId,
        String orderId,
        int price,
        int taxFree,
        int cancelledPrice,
        int cancelledTaxFree,
        String orderName,
        String companyName,
        String gatewayUrl,
        Map<String, Object> metadata,
        boolean sandbox,
        String pg,
        String method,
        String methodSymbol,
        String methodOrigin,
        String methodOriginSymbol,
        String currency,
        String receiptUrl,
        OffsetDateTime purchasedAt,
        OffsetDateTime cancelledAt,
        OffsetDateTime requestedAt,
        String escrowStatusLocale,
        String escrowStatus,
        String statusLocale,
        int status,
        CardData cardData,
        PhoneData phoneData,
        BankData bankData,
        VBankData vbankData
) {

    public record CardData(
            String cardApproveNo,
            String cardNo,
            String cardQuota,
            String cardCompanyCode,
            String cardCompany,
            String receiptUrl
    ) {}

    public record PhoneData(
            String authNo,
            String phone
    ) {}

    public record BankData(
            String bankCode,
            String bankName,
            String cashReceiptNo
    ) {}

    public record VBankData(
            String bankCode,
            String bankName,
            String bankAccount,
            String bankUsername,
            String senderName,
            OffsetDateTime expiredAt,
            String cashReceiptNo
    ) {}

}
