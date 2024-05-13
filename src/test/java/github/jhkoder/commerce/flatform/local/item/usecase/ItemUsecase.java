package github.jhkoder.commerce.flatform.local.item.usecase;

import org.junit.jupiter.api.Test;

public class ItemUsecase {

    @Test
    void 아이템_등록(){
        /*
            1. 아아템 등록까지 이전 루트
                login -> 대쉬 보드 -> [ 상픔 등록 ] ->
                login -> mypage -> [판매자,관리자] 등급 변경 신청

                /mypage/user
                -   권한 변경 기능
                -   비번 변경 가능
                -   이메일,전화번호 추가 인증
                -   이메일, 전화번호 변경 ( 전화번호 등록은 해야 구매 가능)  2차 인증
                -   회원가입 날짜 보기
                /mypage/seller
                [USER 기능 ]
                + 상호 등록
                    + 상호명
                    + 결제 받을 계좌번호
                    + 은행 이름
                /mypage/admin
                [USER 기능]
                +  권리자 요청 목록 조회
                +  권리자 요청 수락
                +  회원 관리 페이지 이도 ㅇ
                /mypage/admin/dashboard
                + 회원 관리 (회원들 현환 보기)
                + 판매자 관리 (판매자들 현황 보기)


                관리자만 판매자,관리자로 권한 변경 가능
                - ADMIN 권한 변경 은 요청후 관리자 허가 해야 가능
                    - 권리자 권한 요청
                - SELLER 권한은 판매자가 되기 위한 정보가 필요함
                    - 폰,에미일 추가 인증 필요

            2. ROLE_SELLER 권한 이 필요

         */

    }

    @Test
    void 아이템 (){

    }

}
