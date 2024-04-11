package github.jhkoder.commerce.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.common.error.ErrorDescriptor;
import github.jhkoder.commerce.common.output.AdocRequest;
import github.jhkoder.commerce.common.output.RestDocAsciidocOutput;
import github.jhkoder.commerce.exception.ErrorCode;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.sshd.common.file.nonefs.NoneFileSystemProvider.SCHEME;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = SCHEME, uriHost = HOST)
@AutoConfigureMockMvc(addFilters = false)
public class RestDocControllerTests {
    public static final String SCHEME = "http";
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

    protected static String strToJson(String id, int value) {
        try {
            Object obj = jsonParser.parse("{\"" + id + "\": " + value + "}");
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("RestDocControllerTests.strToJson.parse ERROR");
        }
    }

    protected static String strToJson(String id, Long value) {
        try {
            Object obj = jsonParser.parse("{\"" + id + "\": " + value + "}");
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("RestDocControllerTests.strToJson.parse ERROR");
        }
    }


    protected void autoDoc(String path, ErrorDescriptor... descriptors) {
        new RestDocAsciidocOutput().out(path, Arrays.asList(descriptors), defaultAdoc());
    }

    protected void autoDoc(String path, AdocRequest request) {
        new RestDocAsciidocOutput().out(path, request);
    }


    protected void autoDoc(String path) {
        new RestDocAsciidocOutput().out(path, null, defaultAdoc());
    }

    public static AdocRequest defaultAdoc() {
        return new AdocRequest(new ArrayList<>(), true, true, true, true);
    }


    protected ResultActions jsonGetWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request)
                .with(csrf())
        );
    }

    protected ResultActions jsonPostWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request)
                .with(csrf())
        );
    }

    protected ResultActions jsonUpdatesWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .patch(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request)
                .with(csrf())
        );
    }

    protected ResultActions jsonUpdateWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request)
                .with(csrf())
        );
    }

    protected ResultActions jsonDeleteWhen(String uri, String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(request)
                .with(csrf())
        );
    }


    protected ResultActions jsonGetWhen(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );
    }

    protected ResultActions jsonPostWhen(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())

        );
    }

    protected ResultActions jsonUpdatesWhen(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .patch(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );
    }

    protected ResultActions jsonUpdateWhen(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .put(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );
    }

    protected ResultActions jsonDeleteWhen(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        );
    }


    public static class CustomAdocBuilder {

        private List<ErrorDescriptor> descriptors = new ArrayList<>();
        private boolean isRequestBody = true;
        private boolean isRequestFields = true;
        private boolean isResponseBody = true;
        private boolean isResponseFields = true;

        public CustomAdocBuilder disabledRequestBody() {
            isRequestBody = false;
            return this;
        }

        public CustomAdocBuilder disabledRequestFields() {
            isRequestFields = false;
            return this;
        }

        public CustomAdocBuilder disabledResponseBody() {
            isResponseBody = false;
            return this;
        }

        public CustomAdocBuilder disabledResponseFields() {
            isResponseFields = false;
            return this;
        }

        public static CustomAdocBuilder bulider() {
            return new CustomAdocBuilder();
        }

        public CustomAdocBuilder adocErrors(ErrorCode... errorCode) {
            this.descriptors.addAll(Arrays.stream(errorCode)
                    .map(ErrorDescriptor::of)
                    .toList());
            return this;
        }

        public CustomAdocBuilder addError(int status, String messaage) {
            this.descriptors.add(new ErrorDescriptor(status, messaage));
            return this;
        }

        public CustomAdocBuilder addErrorValid(int status, String messaage) {
            this.descriptors.add(new ErrorDescriptor(status, messaage));
            return this;
        }

        public AdocRequest build() {
            return new AdocRequest(descriptors, isRequestBody, isRequestFields, isResponseBody, isResponseFields);
        }

    }
}
