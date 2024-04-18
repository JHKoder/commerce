package github.jhkoder.commerce.flatform.local.ep.category.domain;

public enum CategoryLevel {
    TOP("상"),
    MID("중"),
    BOTTOM("하");

    private final String level;
    CategoryLevel(String level) {
        this.level=level;
    }

    public String getLevel() {
        return level;
    }
}
