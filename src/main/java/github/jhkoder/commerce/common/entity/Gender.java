package github.jhkoder.commerce.common.entity;

import lombok.Getter;

@Getter
public enum Gender {
    MAN("남성"),
    WOMAN("여성");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
    public int jdbcInsert(){
        return MAN== this ? 0 : 1;
    }
}