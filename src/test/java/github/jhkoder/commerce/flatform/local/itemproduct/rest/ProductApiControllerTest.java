package github.jhkoder.commerce.flatform.local.itemproduct.rest;

import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomUser;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductDslRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.ItemProductRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.ProductService;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductsCategoryPageResponse;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.web.rest.ProductApiController;
import github.jhkoder.commerce.image.repository.ImageJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(controllers = {
        ProductApiController.class
})
@DisplayName("상품")
public class ProductApiControllerTest extends RestDocControllerTests {

    @MockBean
    private ProductService productService;
    @MockBean
    private ItemProductDslRepository productDslRepository;
    @MockBean
    private ItemProductRepository productRepository;
    @MockBean
    private ImageJpaRepository imageRepository;

    private String api = "/api/all/products";

    @Test
    @WithMockCustomUser
    @DisplayName("특정 카테고리 페이지")
    void categoryPage() throws Exception {
        //givee
        String pathAdoc = "products/pages";
//        ProductCategoryPageRequest pageRequest = new ProductCategoryPageRequest(1L, 1, 10);
//        String request = objectMapper.writeValueAsString(pageRequest);
//        Page<ProductsCategoryPageResponse> responses = getCategoryPageProvider();
//        when(productService.findCategoryPage(any()))
//                .thenReturn(responses);
//
//        ResultActions actions = jsonPostWhen(api + "/category", request);
//
//        // then
//        actions.andExpect(status().isOk())
//                .andDo(document(pathAdoc,
//                        documentRequest(),
//                        requestFields(
//                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("The ID of the category"),
//                                fieldWithPath("page").type(JsonFieldType.NUMBER).description("The page number"),
//                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("The page size")
//                        ),
//                        responseFields(
//                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
//                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
//                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
//                                fieldWithPath("pageable").type(JsonFieldType.STRING).description("INSTANCE"),
//                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
//                                fieldWithPath("sort").description("정렬에 대한 메타데이터"),
//                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 기준이 비어 있는지 여부"),
//                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("결과가 정렬되었는지 여부"),
//                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("결과가 정렬되지 않았는지 여부"),
//                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
//                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
//                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("현재 페이지가 비어 있는지 여부"),
//                                fieldWithPath("content[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
//                                fieldWithPath("content[].name").type(JsonFieldType.STRING).description("상품 이름"),
//                                fieldWithPath("content[].imgLink").type(JsonFieldType.STRING).description("대표 이미지 링크"),
//                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER).description("가격"),
//                                fieldWithPath("content[].originPrice").type(JsonFieldType.NUMBER).description("원래 가격")
//                        )
//
//                ));
//
//        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
//                .build());
    }


    @Test
    @WithMockCustomUser
    @DisplayName("상세보기")
    void product() throws Exception {
        //givee
        String pathAdoc = "products/product";
        when(productService.findProduct(any()))
                .thenReturn(productResponse());

        ResultActions actions = jsonGetPathWhen(api + "/products/{productId}", 1L);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("sellerName").type(JsonFieldType.STRING).description("판매자명"),
                                fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("mainImageUrl").type(JsonFieldType.STRING).description("대표 이미지 URL"),
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("normalPrice").type(JsonFieldType.NUMBER).description("정상 가격"),
                                fieldWithPath("maker").type(JsonFieldType.STRING).description("제조사"),
                                fieldWithPath("origin").type(JsonFieldType.STRING).description("원산지"),
                                fieldWithPath("brand").type(JsonFieldType.STRING).description("브랜드"),
                                fieldWithPath("brandCertification").type(JsonFieldType.BOOLEAN).description("브랜드 인증 여부"),
                                fieldWithPath("barcode").type(JsonFieldType.STRING).description("바코드"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("orderMode").type(JsonFieldType.BOOLEAN).description("주문 가능 여부"),
                                fieldWithPath("rentalInfo").type(JsonFieldType.STRING).description("대여 정보"),
                                fieldWithPath("clickCount").type(JsonFieldType.NUMBER).description("클릭 횟수"),
                                fieldWithPath("reviewCount").type(JsonFieldType.NUMBER).description("리뷰 횟수"),
                                fieldWithPath("minimumPurchaseQuantity").type(JsonFieldType.NUMBER).description("최소 구매 수량"),
                                fieldWithPath("optionDetail").type(JsonFieldType.STRING).description("옵션 상세"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("deliveryPrice").type(JsonFieldType.NUMBER).description("배송비"),
                                fieldWithPath("shippingSetting").type(JsonFieldType.STRING).description("배송 설정"),
                                fieldWithPath("fastDelivery").type(JsonFieldType.BOOLEAN).description("빠른 배송 가능 여부"),
                                fieldWithPath("regularDelivery").type(JsonFieldType.BOOLEAN).description("정기 배송 가능 여부"),
                                fieldWithPath("dawnDelivery").type(JsonFieldType.BOOLEAN).description("새벽 배송 가능 여부"),
                                fieldWithPath("isbn").type(JsonFieldType.STRING).description("ISBN"),
                                fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .build());
    }


    private Page<ProductsCategoryPageResponse> getCategoryPageProvider() {
        Long productId = 123L;
        String imgLink = "https://example.com/image";
        int price = 10000;
        int deliveryPrice = 3000;

        ProductsCategoryPageResponse response =
                new ProductsCategoryPageResponse(productId, "name", imgLink, price, price, deliveryPrice);
        List<ProductsCategoryPageResponse> contentList = Collections.singletonList(response);

        return new PageImpl<>(contentList);
    }

    public ProductResponse productResponse() {
        return new ProductResponse(
                "TestSeller",
                123L,
                456L,
                789L,
                "https://example.com/image",
                List.of("","",""),
                "TestItem",
                10000,
                "TestMaker",
                "TestOrigin",
                "TestBrand",
                true,
                "TestBarcode",
                9000,
                true,
                "TestRentalInfo",
                50,
                20,
                5,
                "TestOptionDetail",
                "TestGender",
                3000,
                "TestShippingSetting",
                true,
                false,
                true,
                "TestISBN",
                100,
                List.of("2/")
        );
    }
}
