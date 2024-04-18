package github.jhkoder.commerce.store.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import github.jhkoder.commerce.common.RestDocControllerTests;
import github.jhkoder.commerce.common.auth.WithMockCustomSeller;
import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.repository.dto.StoreSellerProductsPageResponse;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.service.ImageService;
import github.jhkoder.commerce.image.service.request.ImageUpdateRequest;
import github.jhkoder.commerce.store.service.StoreSellerService;
import github.jhkoder.commerce.store.service.request.StoreAddProductRequest;
import github.jhkoder.commerce.store.service.request.StoreUpdateProductRequest;
import github.jhkoder.commerce.store.service.response.StoreSellerProductResponse;
import github.jhkoder.commerce.store.web.rest.StoreSellerApiController;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static fixture.ImageFixture.getFixtureImages;
import static fixture.ImageFixture.getFixtureMockMultipartFile;
import static github.jhkoder.commerce.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(StoreSellerApiController.class)
@DisplayName("판매자 스토어")
public class StoreSellerApiControllerTest extends RestDocControllerTests {
    @MockBean
    private StoreSellerService storeService;
    @MockBean
    private ImageService imageService;

    private final String api = "/api/seller/store";

    @Test
    @WithMockCustomSeller
    @DisplayName("상품 추가 API")
    public void addProductTest() throws Exception {
        //given
        String pathAdoc = "store/seller/product/add";
        ItemProduct itemProduct = getProduct();

        List<Images> images = new ArrayList<>();
        images.add(getFixtureImages());
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        StoreAddProductRequest storeAddProductRequest = createProductProvider();
        String request = objectMapper.writeValueAsString(storeAddProductRequest);

        when(storeService.add(anyString(), any())).thenReturn(itemProduct);
        when(imageService.upload(any(ItemProduct.class), any(StoreAddProductRequest.CustomMultiPartFile.class)))
                .thenReturn(images);
        doNothing().when(storeService).updateProductImages(any(), anyList());

        ResultActions actions = jsonPostWhen(api + "/product", request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                        documentRequest(),
                        requestFields(

                                fieldWithPath("item").type(JsonFieldType.OBJECT).description("상품 정보"),
                                fieldWithPath("item.name").type(JsonFieldType.STRING).description("상품 이름"),
                                fieldWithPath("item.normalPrice").type(JsonFieldType.NUMBER).description("정상 가격"),
                                fieldWithPath("item.maker").type(JsonFieldType.STRING).description("제조사"),
                                fieldWithPath("item.origin").type(JsonFieldType.STRING).description("원산지"),
                                fieldWithPath("item.brand").type(JsonFieldType.STRING).description("브랜드"),
                                fieldWithPath("item.brandCertification").type(JsonFieldType.BOOLEAN).description("브랜드 인증 여부"),
                                fieldWithPath("item.barcode").type(JsonFieldType.STRING).description("바코드"),

                                fieldWithPath("product").type(JsonFieldType.OBJECT).description("상품 상세 정보"),
                                fieldWithPath("product.categoryId").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                                fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("product.orderMode").type(JsonFieldType.BOOLEAN).description("주문 모드"),
                                fieldWithPath("product.rentalInfo").type(JsonFieldType.STRING).description("대여 정보"),
                                fieldWithPath("product.clickCount").type(JsonFieldType.NUMBER).description("클릭 수"),
                                fieldWithPath("product.reviewCount").type(JsonFieldType.NUMBER).description("리뷰 수"),
                                fieldWithPath("product.minimumPurchaseQuantity").type(JsonFieldType.NUMBER).description("최소 구매 수량"),
                                fieldWithPath("product.optionDetail").type(JsonFieldType.STRING).description("옵션 상세 정보"),
                                fieldWithPath("product.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("product.deliveryPrice").type(JsonFieldType.NUMBER).description("배송 비용"),
                                fieldWithPath("product.shippingSetting").type(JsonFieldType.STRING).description("배송 설정"),
                                fieldWithPath("product.fastDelivery").type(JsonFieldType.BOOLEAN).description("빠른 배송 여부"),
                                fieldWithPath("product.regularDelivery").type(JsonFieldType.BOOLEAN).description("일반 배송 여부"),
                                fieldWithPath("product.dawnDelivery").type(JsonFieldType.BOOLEAN).description("새벽 배송 여부"),
                                fieldWithPath("product.isbn").type(JsonFieldType.STRING).description("ISBN"),
                                fieldWithPath("product.stock").type(JsonFieldType.NUMBER).description("재고 수량"),

                                fieldWithPath("images.image").type(JsonFieldType.ARRAY).description("이미지 배열")
                        )
                ));

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(CATEGORY_NOT_FOUND,
                        USER_NOT_FOUND,
                        IMAGE_REMOTE_SESSION,
                        IMAGE_REMOTE_UPLOAD)
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("물품 리스트 조회 페이징 API")
    public void findProductsTest() throws Exception {
        //given
        String pathAdoc = "store/seller/product/page";
        List<StoreSellerProductsPageResponse> content = new ArrayList<>();
        content.add(new StoreSellerProductsPageResponse(1L, "Product A", 10, 1000));
        content.add(new StoreSellerProductsPageResponse(2L, "Product B", 15, 2000));
        content.add(new StoreSellerProductsPageResponse(3L, "Product C", 20, 3000));
        Page<StoreSellerProductsPageResponse> response = new PageImpl<>(content);

        when(storeService.findPageProducts(any(), anyInt()))
                .thenReturn(response);
        ResultActions actions = jsonGetPathWhen(api + "/products/page/{page}", 1);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                                documentRequest(),
                                documentResponse(),
                                pathParameters(
                                        parameterWithName("page").description("페이지번호")
                                ),
                                responseFields(
                                        fieldWithPath("content").type(JsonFieldType.ARRAY).description("상품 목록"),
                                        fieldWithPath("content[].itemId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                        fieldWithPath("content[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("content[].stock").type(JsonFieldType.NUMBER).description("재고"),
                                        fieldWithPath("content[].price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("pageable").ignored().description("페이지 정보"),
                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 상품 수"),
                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬된 여부"),
                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않는 여부"),
                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지의 요소 수"),
                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("비어 있는지 여부")
                                )
                        )
                );

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(USER_NOT_FOUND)
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("물품 상세 조회 API")
    public void findProductTest() throws Exception {
        //given
        String pathAdoc = "store/seller/product/id";
        StoreSellerProductResponse testResponse = new StoreSellerProductResponse(
                new StoreSellerProductResponse.StoreSellerItem("Product A", 1000, "Maker", "Origin", "Brand", true, "1234567890"),
                new StoreSellerProductResponse.StoreSellerProduct(
                        2000, true, "Rental Info", 50, 20, 5, "Option Detail", Gender.MAN, 5000,
                        "Shipping Setting", true, false, true, "ISBN123", 100
                )
        );
        when(storeService.findProduct(any(), anyLong()))
                .thenReturn(testResponse);

        ResultActions actions = jsonGetPathWhen(api + "/products/{productId}", 1);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                                documentRequest(),
                                documentResponse(),
                                pathParameters(
                                        parameterWithName("productId").description("상품 ID")
                                ),
                                responseFields(
                                        fieldWithPath("item").type(JsonFieldType.OBJECT).description("상품 정보"),
                                        fieldWithPath("item.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("item.normalPrice").type(JsonFieldType.NUMBER).description("정상 가격"),
                                        fieldWithPath("item.maker").type(JsonFieldType.STRING).description("제조사"),
                                        fieldWithPath("item.origin").type(JsonFieldType.STRING).description("원산지"),
                                        fieldWithPath("item.brand").type(JsonFieldType.STRING).description("브랜드"),
                                        fieldWithPath("item.brandCertification").type(JsonFieldType.BOOLEAN).description("브랜드 인증 여부"),
                                        fieldWithPath("item.barcode").type(JsonFieldType.STRING).description("바코드"),
                                        fieldWithPath("product").type(JsonFieldType.OBJECT).description("상품 상세 정보"),
                                        fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("product.orderMode").type(JsonFieldType.BOOLEAN).description("주문 모드"),
                                        fieldWithPath("product.rentalInfo").type(JsonFieldType.STRING).description("대여 정보"),
                                        fieldWithPath("product.clickCount").type(JsonFieldType.NUMBER).description("클릭 수"),
                                        fieldWithPath("product.reviewCount").type(JsonFieldType.NUMBER).description("리뷰 수"),
                                        fieldWithPath("product.minimumPurchaseQuantity").type(JsonFieldType.NUMBER).description("최소 구매 수량"),
                                        fieldWithPath("product.optionDetail").type(JsonFieldType.STRING).description("옵션 세부 정보"),
                                        fieldWithPath("product.gender").type(JsonFieldType.STRING).description("성별"),
                                        fieldWithPath("product.deliveryPrice").type(JsonFieldType.NUMBER).description("배송 가격"),
                                        fieldWithPath("product.shippingSetting").type(JsonFieldType.STRING).description("배송 설정"),
                                        fieldWithPath("product.fastDelivery").type(JsonFieldType.BOOLEAN).description("빠른 배송 여부"),
                                        fieldWithPath("product.regularDelivery").type(JsonFieldType.BOOLEAN).description("정기 배송 여부"),
                                        fieldWithPath("product.dawnDelivery").type(JsonFieldType.BOOLEAN).description("새벽 배송 여부"),
                                        fieldWithPath("product.isbn").type(JsonFieldType.STRING).description("ISBN"),
                                        fieldWithPath("product.stock").type(JsonFieldType.NUMBER).description("재고")
                                )
                        )
                );

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(PRODUCT_NOT_FOUND,
                        PRODUCT_NOT_SELLER_USER)
                .disabledRequestFields()
                .build());
    }

    @Test
    @WithMockCustomSeller
    @DisplayName("물품 수정 API")
    public void updateProductTest() throws Exception {
        //given
        String pathAdoc = "store/seller/product/update";
        List<ImageUpdateRequest> imageUpdateRequests = new ArrayList<>();
        imageUpdateRequests.add(new ImageUpdateRequest(1L, true));

        StoreUpdateProductRequest productRequest = new StoreUpdateProductRequest(
                new StoreUpdateProductRequest.StoreSellerItem("Product A", 1000, "Maker", "Origin", "Brand", true, "1234567890"),
                new StoreUpdateProductRequest.StoreSellerProduct(
                        2000, true, "Rental Info", 50, 20, 5, "Option Detail",
                        Gender.MAN, 5000, "Shipping Setting", true, false, true, "ISBN123", 100
                ),
                imageUpdateRequests
        );

        doNothing().when(storeService).updateProduct(any(), any(), anyLong());
        doNothing().when(imageService).change(any(), anyList());

        String request = objectMapper.writeValueAsString(productRequest);
        ResultActions actions = jsonUpdatePathAndJsonWhen(api + "/products/{productId}", 1, request);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                                documentRequest(),
                                pathParameters(
                                        parameterWithName("productId").description("상품 ID")
                                ),
                                requestFields(
                                        fieldWithPath("item").type(JsonFieldType.OBJECT).description("상품 정보"),
                                        fieldWithPath("item.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("item.normalPrice").type(JsonFieldType.NUMBER).description("정상 가격"),
                                        fieldWithPath("item.maker").type(JsonFieldType.STRING).description("제조사"),
                                        fieldWithPath("item.origin").type(JsonFieldType.STRING).description("원산지"),
                                        fieldWithPath("item.brand").type(JsonFieldType.STRING).description("브랜드"),
                                        fieldWithPath("item.brandCertification").type(JsonFieldType.BOOLEAN).description("브랜드 인증 여부"),
                                        fieldWithPath("item.barcode").type(JsonFieldType.STRING).description("바코드"),
                                        fieldWithPath("product").type(JsonFieldType.OBJECT).description("상품 상세 정보"),
                                        fieldWithPath("product.price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("product.orderMode").type(JsonFieldType.BOOLEAN).description("주문 모드"),
                                        fieldWithPath("product.rentalInfo").type(JsonFieldType.STRING).description("대여 정보"),
                                        fieldWithPath("product.clickCount").type(JsonFieldType.NUMBER).description("클릭 수"),
                                        fieldWithPath("product.reviewCount").type(JsonFieldType.NUMBER).description("리뷰 수"),
                                        fieldWithPath("product.minimumPurchaseQuantity").type(JsonFieldType.NUMBER).description("최소 구매 수량"),
                                        fieldWithPath("product.optionDetail").type(JsonFieldType.STRING).description("옵션 세부 정보"),
                                        fieldWithPath("product.gender").type(JsonFieldType.STRING).description("성별"),
                                        fieldWithPath("product.deliveryPrice").type(JsonFieldType.NUMBER).description("배송 가격"),
                                        fieldWithPath("product.shippingSetting").type(JsonFieldType.STRING).description("배송 설정"),
                                        fieldWithPath("product.fastDelivery").type(JsonFieldType.BOOLEAN).description("빠른 배송 여부"),
                                        fieldWithPath("product.regularDelivery").type(JsonFieldType.BOOLEAN).description("정기 배송 여부"),
                                        fieldWithPath("product.dawnDelivery").type(JsonFieldType.BOOLEAN).description("새벽 배송 여부"),
                                        fieldWithPath("product.isbn").type(JsonFieldType.STRING).description("ISBN"),
                                        fieldWithPath("product.stock").type(JsonFieldType.NUMBER).description("재고"),
                                        fieldWithPath("file").type(JsonFieldType.ARRAY).description("이미지 파일 리스트"),
                                        fieldWithPath("file[].imageId").type(JsonFieldType.NUMBER).description("이미지 ID"),
                                        fieldWithPath("file[].image").type(JsonFieldType.NULL).description("이미지 파일"),
                                        fieldWithPath("file[].update").type(JsonFieldType.BOOLEAN).description("이미지 업데이트 여부")
                                )
                        )
                );

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(PRODUCT_NOT_FOUND,
                        PRODUCT_NOT_SELLER_USER,
                        IMAGE_NOT_ID,
                        IMAGE_AUTHOR_MISMATCH,
                        IMAGE_REMOTE_UPLOAD)
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }


    @Test
    @WithMockCustomSeller
    @DisplayName("물품 삭제 API 테스트")
    public void deleteProductTest() throws Exception {
        String pathAdoc = "store/seller/product/delete";
        when(storeService.deleteProduct(any(), anyLong()))
                .thenReturn(new ArrayList<>());

        doNothing().when(imageService).delete(anyList());
        ResultActions actions = jsonDeletePathWhen(api + "/products/{productId}", 1);

        // then
        actions.andExpect(status().isOk())
                .andDo(document(pathAdoc,
                                documentRequest(),
                                pathParameters(
                                        parameterWithName("productId").description("상품 ID")
                                )
                        )
                );

        autoDoc(pathAdoc, CustomAdocBuilder.bulider()
                .adocErrors(PRODUCT_NOT_FOUND,
                        PRODUCT_NOT_SELLER_USER,
                        IMAGE_REMOTE_DELETE_FAIL)
                .disabledRequestFields()
                .disabledRequestBody()
                .disabledResponseBody()
                .disabledResponseFields()
                .build());
    }


    private StoreAddProductRequest createProductProvider() {
        // StoreItemRequest 생성
        StoreAddProductRequest.StoreItemRequest storeItemRequest = new StoreAddProductRequest.StoreItemRequest(
                "Sample Product",
                10000,
                "Sample Maker",
                "Sample Origin",
                "Sample Brand",
                true,
                "Sample Barcode"
        );

        // StoreProductRequest 생성
        StoreAddProductRequest.StoreProductRequest storeProductRequest = new StoreAddProductRequest.StoreProductRequest(
                1L, // categoryId
                15000, // price
                false, // orderMode
                "Sample Rental Info",
                10, // clickCount
                5, // reviewCount
                2, // minimumPurchaseQuantity
                "Sample Option Detail",
                Gender.MAN, // gender
                3000, // deliveryPrice
                "Sample Shipping Setting",
                true, // fastDelivery
                false, // regularDelivery
                true, // dawnDelivery
                "Sample ISBN",
                20 // stock
        );

        StoreAddProductRequest.CustomMultiPartFile customMultiPartFile = getFixtureMockMultipartFile();

        // StoreAddProductRequest 생성
        return new StoreAddProductRequest(storeItemRequest, storeProductRequest, customMultiPartFile);
    }

    private ItemProduct getProduct() {
        User user = new User("testUser", "testname", "password12", "test@gmail.com", "01012341234", Gender.MAN, Role.USER);
        Category category = new Category("TestCategory",null);
        Item item = new Item("TestItem", 100, "TestMaker", "TestOrigin", "TestBrand",
                true, "TestBarcode");
        List<Images> links = new ArrayList<>();
        int price = 200;
        boolean orderMode = true;
        String rentalInfo = "TestRentalInfo";
        int clickCount = 5;
        int reviewCount = 10;
        int minimumPurchaseQuantity = 1;
        String optionDetail = "TestOptionDetail";
        Gender gender = Gender.MAN;
        int deliveryPrice = 10;
        String shippingSetting = "TestShippingSetting";
        boolean fastDelivery = true;
        boolean regularDelivery = true;
        boolean dawnDelivery = false;
        String isbn = "TestISBN";
        int stock = 20;

        return new ItemProduct(user, category, item, links, price, orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
    }

}
