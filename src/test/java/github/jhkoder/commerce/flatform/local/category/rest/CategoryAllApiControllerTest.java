package github.jhkoder.commerce.flatform.local.category.rest;

import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomAdmin;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryTopResponse;
import github.jhkoder.commerce.flatform.local.ep.category.web.rest.CategoryAllApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@WithMockCustomAdmin
@WebMvcTest(controllers = {
        CategoryAllApiController.class
})
@DisplayName("카테고리 api")
public class CategoryAllApiControllerTest extends RestDocControllerTests {


    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private final String api = "/api/all/category";

    @Test
    @DisplayName("전체 탐색")
    void find() throws Exception {
        //given
        String pathAdoc = "category/all/find";

        when(categoryService.findAll())
                .thenReturn(getFindProvider());

        // when
        ResultActions actions = jsonGetWhen(api+"/all");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentResponse(),
                        responseFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("상위 카테고리의 이름"),
                                fieldWithPath("[].categoryId").type(JsonFieldType.NUMBER).description("상위 카테고리의 ID"),
                                fieldWithPath("[].middleCategories").type(JsonFieldType.ARRAY).description("중간 카테고리 리스트"),
                                fieldWithPath("[].middleCategories[].name").type(JsonFieldType.STRING).description("중간 카테고리의 이름"),
                                fieldWithPath("[].middleCategories[].categoryId").type(JsonFieldType.NUMBER).description("중간 카테고리의 ID"),
                                fieldWithPath("[].middleCategories[].smallCategories").type(JsonFieldType.ARRAY).description("소 카테고리 리스트"),
                                fieldWithPath("[].middleCategories[].smallCategories[].name").type(JsonFieldType.STRING).description("소 카테고리의 이름"),
                                fieldWithPath("[].middleCategories[].smallCategories[].categoryId").type(JsonFieldType.NUMBER).description("소 카테고리의 ID")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .disabledRequestFields()
                .build());
    }



    @Test
    @DisplayName("상위 탐색")
    void findTop() throws Exception {
        //given
        String pathAdoc = "category/all/findTop";

        when(categoryService.findTopCategory())
                .thenReturn(getFindTopProvider());

        // when
        ResultActions actions = jsonGetWhen(api+"/top");

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentResponse(),
                        responseFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("상위 카테고리의 이름"),
                                fieldWithPath("[].categoryId").type(JsonFieldType.NUMBER).description("상위 카테고리의 ID")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .disabledRequestFields()
                .build());
    }

    private List<CategoryTopResponse> getFindTopProvider() {
        List<CategoryTopResponse> responses = new ArrayList<>();

        CategoryTopResponse top1 = new CategoryTopResponse("top Category1",1L);
        CategoryTopResponse top2 = new CategoryTopResponse("top Category1",2L);
        responses.add(top1);
        responses.add(top2);
        return responses;
    }
    private List<CategoryResponse> getFindProvider() {
        List<CategoryResponse> responses = new ArrayList<>();

        CategoryResponse.MiddleCategory middle1 = new CategoryResponse.MiddleCategory("mid Category1", 1L, createSmallCategories());
        CategoryResponse.MiddleCategory middle2 = new CategoryResponse.MiddleCategory("mid Category2", 2L, createSmallCategories());
        CategoryResponse.MiddleCategory middle3 = new CategoryResponse.MiddleCategory("mid Category3", 3L, createSmallCategories());

        List<CategoryResponse.MiddleCategory> middleCategories1 = List.of(middle1, middle2);
        List<CategoryResponse.MiddleCategory> middleCategories2 = List.of(middle3);

        CategoryResponse categoryResponse1 = new CategoryResponse("top Category1", 1L, middleCategories1);
        CategoryResponse categoryResponse2 = new CategoryResponse("top Category2", 2L, middleCategories2);

        responses.add(categoryResponse1);
        responses.add(categoryResponse2);

        return responses;
    }

    private List<CategoryResponse.SmallCategory> createSmallCategories() {
        List<CategoryResponse.SmallCategory> smallCategories = new ArrayList<>();

        CategoryResponse.SmallCategory small1 = new CategoryResponse.SmallCategory("bottom Category1", 1L);
        CategoryResponse.SmallCategory small2 = new CategoryResponse.SmallCategory("bottom Category2", 2L);
        CategoryResponse.SmallCategory small3 = new CategoryResponse.SmallCategory("bottom Category3", 3L);

        smallCategories.add(small1);
        smallCategories.add(small2);
        smallCategories.add(small3);

        return smallCategories;
    }
}
