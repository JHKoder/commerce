package github.jhkoder.commerce.flatform.local.ep.category.service.request;

import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;

public record CategoryAddRequest(String name, CategoryLevel level, boolean isTop, Long topId) {
}
