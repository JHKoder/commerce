package github.jhkoder.commerce.flatform.local.itemevent;

public class eventTest {
    /*
        판매자가 이벤트 추가,수정,삭제,보기, 전체 보기 , 상세 보기

        사용자가 이벤트 보기,전체보기,검색하기,카테고리 & 검색하기


        이벤트 종류
        [쿠폰],[가격 할인]

        쿠폰은 랜덤값이며 정해진 16가지 값을 상품과 일치할지 가격 할인이 가능

        가격 할인은 상품 자체에 할인 가격이 포함되어야 함

        등록 방법

        쿠폰 등록
        -> 상품 쿠폰 이벤트 등록
        -> 쿠폰 발급 개수 , [할인 가격,할인 퍼센트] 발급시 쿠폰 값을 발급받은 쿠폰 공통 값 따로 분리된 값 두가지 존재 함
        -> 100 개 이상 발급 개수면
        -> QR 코드 발급
        -> QR image 는 /images/event/coupon/qr/cert_akldfaawaeiofsdo;fjwsoivmsdifonweoifsdjvdsfjweiofndsaiavojewfdsnjf sdfonwefsdfaseof

        ->쿠폰은 : 암호화된 18가지 랜덤값, QR 코드

        -> 쿠폰 QR 사용시
        서버에 -> 특정 유저 ID 가 해당 쿠폰을 사용중  상태로 변경되고
        특정 유저는 상품을 할인된 가격만큼 구매할 수 있도록 함

        -> 쿠폰 images 는 암호화 되어 보여짐
        entity{
            qrlink:
            qr

            productId,
            eventId,
        }

        qr 코드 생성
        - 이미지 크기 지정
        -> 필수
        코드내 저장할 텍스트  URL 인코딩 - 최소 문자수 1 최대문자수 900
        , 짧을 수록 좋음
        -> int x int (선택)
        -> 최대값 1000x1000       - png, fig,jpeg,jpg
                 1000000x1000000 - svg,eps
        ->

        (로그인 상태)
        (비로그인 상태)
        -> 로그인 창 으로 이동
        -> uri :/event/coupon/{UUID 18 가지 암호화 값} 으로 이동
        -> controller 에서
        ->     로그인 이면 /login 후 결제 사이트 접근

        할인 가격
        -> origin price 실제가격
        -> price 할인된 가격
        -> Event 에 상세 성명

     */
}
