package github.jhkoder.commerce.security.service.dto;


import github.jhkoder.commerce.user.domain.Role;

import java.util.List;


public record TokenParserResponse(String username, List<Role> roles) {
}
