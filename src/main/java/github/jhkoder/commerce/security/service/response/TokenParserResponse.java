package github.jhkoder.commerce.security.service.response;

import github.jhkoder.commerce.user.domain.Role;

import java.util.List;


public record TokenParserResponse(String username, List<Role> roles) {
}
