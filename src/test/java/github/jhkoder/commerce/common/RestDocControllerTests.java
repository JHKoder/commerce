package github.jhkoder.commerce.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.user.web.rest.SignUpApiController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import static org.apache.sshd.common.file.nonefs.NoneFileSystemProvider.SCHEME;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest(controllers = {
        SignUpApiController.class
})
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = SCHEME, uriHost = HOST)
@AutoConfigureMockMvc(addFilters = false)
public class RestDocControllerTests {

    // 여기서 문서에 표시될 정보들을 정의
    public static final String SCHEME = "https";
    public static final String HOST = "io.github.shirohoo";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;


    // 나중에 테스트 코드 중 문서작성부에 사용될 편의성 메서드들을 정의
    protected static OperationRequestPreprocessor documentRequest() {
        return Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                        .scheme(SCHEME)
                        .host(HOST)
                        .removePort(),
                prettyPrint());
    }

    protected static OperationResponsePreprocessor documentResponse() {
        return Preprocessors.preprocessResponse(prettyPrint());
    }

    protected static StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

    protected static ContentResultMatchers content() {
        return MockMvcResultMatchers.content();
    }

    protected static Attributes.Attribute optional() {
        return new Attributes.Attribute("optional", "false"); //default true
    }
}
