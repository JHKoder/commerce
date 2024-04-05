package github.jhkoder.commerce.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.common.error.ErrorDescriptor;
import github.jhkoder.commerce.user.web.rest.SignUpApiController;
import net.minidev.json.parser.JSONParser;
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

import java.util.Arrays;

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
    public static final String SCHEME = "https";
    public static final String HOST = "jhkoder-ecommerce.shop";
    protected static ObjectMapper objectMapper = new ObjectMapper();
    protected static JSONParser jsonParser = new JSONParser();

    @Autowired
    protected MockMvc mockMvc;

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

    protected static String strToJson(String id, String value) {
        try {
            Object obj = jsonParser.parse("{\"" + id + "\": \"" + value + "\"}");
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("RestDocControllerTests.strToJson.parse ERROR");
        }
    }

    protected void autoDoc(String path, ErrorDescriptor... descriptors) {
        new AdocOutput().out(path, Arrays.asList(descriptors));
    }

    protected void autoDoc(String path) {
        new AdocOutput().out(path, null);
    }
}
