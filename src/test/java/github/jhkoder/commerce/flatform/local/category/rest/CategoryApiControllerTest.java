package github.jhkoder.commerce.flatform.local.category.rest;


import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomAdmin;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryAddRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryChangeRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryUpdateRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import github.jhkoder.commerce.flatform.local.ep.category.web.rest.CategoryApiController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static github.jhkoder.commerce.exception.ErrorCode.*;
import static github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WithMockCustomAdmin
@WebMvcTest(controllers = {
        CategoryApiController.class
})
@DisplayName("관리자용 카테고리")
public class CategoryApiControllerTest extends RestDocControllerTests {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private final String adminApi = "/api/admin/category";

    @Test
    @DisplayName("전체 탐색")
    void find() throws Exception {
        //given
        String pathAdoc = "category/admin/find";

        when(categoryService.findAll())
                .thenReturn(getFindProvider());

        // when
        ResultActions actions = jsonGetWhen(adminApi);

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
    @DisplayName("추가")
    void add() throws Exception {
        //given
        String pathAdoc = "category/admin/add";
        CategoryAddRequest categoryAddRequest = new CategoryAddRequest("", CategoryLevel.TOP, false, 0L);
        String request = objectMapper.writeValueAsString(categoryAddRequest);
        doNothing().when(categoryService).add(categoryAddRequest);

        // when
        ResultActions actions = jsonPostWhen(adminApi, request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 명"),
                                fieldWithPath("level").type("Enum: [ " + TOP + "," + MID + "," + BOTTOM + " ]").description( " 카테고리 레벨"+
                                        "TOP : 상위 카테고리 \n" +
                                                "MID : 중위 카테고리 \n" +
                                                "BOTTOM : 하위 카테고리 "),
                                fieldWithPath("isTop").type(JsonFieldType.BOOLEAN).description("상위 카테고리 여부"),
                                fieldWithPath("topId").type("LONG 타입").description("상위 카테고리 ID 값 ").attributes(optional())
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        CATEGORY_NOT_TOP_ID
                )
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }

    @Test
    @DisplayName("이름 변경")
    void update() throws Exception {
        //given
        String pathAdoc = "category/admin/update";
        CategoryUpdateRequest categoryAddRequest = new CategoryUpdateRequest(0L,"");
        String request = objectMapper.writeValueAsString(categoryAddRequest);
        doNothing().when(categoryService).update(categoryAddRequest);

        // when
        ResultActions actions = jsonUpdatesWhen(adminApi, request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 ID 값"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 명")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        CATEGORY_NOT_FOUND
                )
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }

    @Test
    @DisplayName("삭제")
    void delete() throws Exception {
        //given
        String pathAdoc = "category/admin/delete";
        doNothing().when(categoryService).delete(1L);

        // when
        ResultActions actions = jsonDeletePathWhen(adminApi+"/{categoryId}", 1L);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 ID")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        CATEGORY_NOT_FOUND
                )
                .disabledRequestFields()
                .disabledResponseFields()
                .build());
    }

    @Test
    @DisplayName("이동")
    void change() throws Exception {
        //given
        String pathAdoc = "category/admin/move";
        CategoryChangeRequest categoryAddRequest = new CategoryChangeRequest(1L,2L,1,0);
        String request = objectMapper.writeValueAsString(categoryAddRequest);

        doNothing().when(categoryService).move(categoryAddRequest);

        // when
        ResultActions actions = jsonUpdatesWhen(adminApi+"/move",request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(
                                fieldWithPath("topId").type(JsonFieldType.NUMBER).description("상위 카테고리 Id"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("이동할 카테고리 id"),
                                fieldWithPath("up").type(JsonFieldType.NUMBER).description("index 올리기"),
                                fieldWithPath("down").type(JsonFieldType.NUMBER).description("index 내리기")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(
                        CATEGORY_NOT_FOUND,
                        CATEGORY_MOVE_UP_INDEX_OUT,
                        CATEGORY_MOVE_DOWN_INDEX_OUT,
                        CATEGORY_CHANGE_NOT_MOVE
                )
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
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
