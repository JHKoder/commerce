package github.jhkoder.commerce.flatform.local.ep.category.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum CategoryLevel {
    TOP("상"),
    MID("중"),
    BOTTOM("하");

    private final String level;
    CategoryLevel(String level) {
        this.level=level;
    }

}
