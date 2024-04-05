package github.jhkoder.commerce.common.error;

public class ErrorDescriptor {

    private final int code;
    private final String about;

    public ErrorDescriptor(int code, String about) {
        this.code = code;
        this.about = about;
    }

    public int getCode() {
        return code;
    }

    public String getAbout() {
        return about;
    }
}
