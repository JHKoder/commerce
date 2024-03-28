package github.jhkoder.commerce.flatform.naver.ep.product.domain;

import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "index")
    private Long index;

    @Column(name = "id", nullable = false,length = 50)
    private String id; //  상품 ID ,size= 50 ex) A12345

    @Column(name = "title", nullable = false,length = 100)
    private String title; // 상품 명

    @Column(name = "price_pc", nullable = false,length = 9)
    private int pricePc; // 상품 가격

    @Column(name = "price_mobile",length = 9)
    private int priceMobile; // 모바일 상품 가격

    @Column(name = "normal_price",length =9)
    private int normalPrice; // 정가

    @Column(name = "link", nullable = false)
    private String link; // 상품 URL

    @Column(name = "mobile_link")
    private String mobileLink;// 상품 모바일 URL

    @Column(name = "image_link", nullable = false)
    private String imageLink; //이미지 링크

    @Column(name = "add_image_link",length = 2000)
    private String addImageLink; //추가 이미지

    @Column(name = "video_url",length = 2000)
    private String videoUrl; //상품 동영상
    @Column(name = "naver_category",length = 8)
    private String naver_category ; //네이버 카테고리

    @Column(name = "naver_product_id",length = 50)
    private String naver_product_id; //카탈로그 페이지 구매조선ID

    @Column(name = "product_option_id",length = 8)
    private String product_option_id ; //상품 동영상


    @Column(name = "condition", nullable = false,length = 10)
    private String condition; // 상품 상태"신상품,중고 텍스 값만 허용

    @Column(name = "import_flag", nullable = false)
    private boolean importFlag; // 해외구매대행 여부 Y,N

    @Column(name = "parallel_import", nullable = false)
    private boolean parallelImport;// 병행수입 여부 Y N

    @Column(name = "order_made", nullable = false)
    private boolean orderMade; // 주문제작상품 여부

    @Column(name = "product_flag", nullable = false,length = 10)
    private String productFlag; // 판매방식 구분

    @Column(name = "adult", nullable = false)
    private boolean adult; //미성년자 구매 불가 상품 여부

    @Column(name = "goos_type",length = 10)
    private String goosType; //상품 구분

    @Column(name = "barcode",length = 13)
    private String barcode; //바코드

    @Column(name = "manufacture_define_number",length = 100)
    private String manufactureDefineNumber; //제품코드

    @Column(name = "brand",length = 60)
    private String brand; //브랜드

    @Column(name = "brand_certification")
    private boolean brandCertification ; //브랜드 인증 상품 여부

    @Column(name = "maker",length = 60)
    private String maker; //제조사

    @Column(name = "origin",length = 30)
    private String origin; //원산지
    @Column(name = "installation_costs")
    private boolean installationCosts; //별도 성피비 유무
    @Column(name = "rental_info",length = 13)
    private String rental_info; //렌탈정보

    @Column(name = "search_tag",length = 100)
    private String searchTag; //검색 태그

    @Column(name = "group_id",length = 50)
    private String groupId; //그룹 ID
    @Column(name = "vendor_id",length = 500)
    private String vendorId; //제휴사 상품 id
    @Column(name = "coordi_id",length = 500)
    private String coordiId; //코디 상품
    @Column(name = "review_count",length = 10)
    private int reviewCount; //리뷰 카운트



    @Column(name = "minimum_purchase_quantity", nullable = false)
    private int minimumPurchaseQuantity; // 최소구매수량

    @Column(name = "attribute",length = 500)
    private String attribute; // 속성
    @Column(name = "option_detail",length = 1000)
    private String option_detail; // 구매옵션


    @Column(name = "seller_id",length = 50)
    private String sellerId; // 셀러ID

    @Column(name = "age_group",length = 10)
    private String ageGroup; // 주 이용 고객층

    @Column(name = "gender",length = 10)
    private String gender; // 성별

    @Column(name = "shipping_settings",length = 100)
    private String shippingSettings; // 배송부가 정보

    @Column(name = "fast_delivery", nullable = false,length = 100)
    private boolean fastDelivery; // 빠른배송 여부

    @Column(name = "regular_delivery", nullable = false)
    private boolean regularDelivery; // 정기배송 여부

    @Column(name = "dawn_delivery", nullable = false,length = 50)
    private boolean dawnDelivery;// 새벽 배송 여부

    @Column(name = "npay_unable", nullable = false,length = 3)
    private boolean npayUnable;// 네이버 페이 사용불가

    @Column(name = "npay_unable_acum", nullable = false,length = 3)
    private boolean npayUnableAcum; // 네이버 페이 적립 불가

    @Column(name = "isbn",length = 30)
    private String isbn; //ISBN코드(도서상품)

}