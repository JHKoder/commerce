package github.jhkoder.commerce.common.output;

import github.jhkoder.commerce.common.error.ErrorDescriptor;
import github.jhkoder.commerce.user.domain.Role;

import java.util.List;

public record AdocRequest(
        List<ErrorDescriptor> descriptorList,
        boolean isRequestBody,boolean isRequestFields, boolean isResponseBody,boolean isResponseFields
        ) {
}
