package github.jhkoder.commerce.common.error;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.IgnorableDescriptor;

public class ErrorDescriptor extends IgnorableDescriptor<FieldDescriptor> {

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
