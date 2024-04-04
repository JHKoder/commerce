package github.jhkoder.commerce.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public enum OracleBoolean {
    T(true),
    F(false);
    private final boolean bool;
}
