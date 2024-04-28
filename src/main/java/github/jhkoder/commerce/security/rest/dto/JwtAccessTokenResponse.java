package github.jhkoder.commerce.security.rest.dto;

import github.jhkoder.commerce.user.domain.Role;

import java.util.List;

public record JwtAccessTokenResponse( String username,List<Role> authentications) {

}
