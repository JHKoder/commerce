# 소개

###### Spring boot, Java 기반 백엔드 

---

**e-commerce** 는 홈쇼핑 사이트 입니다.

쉽게 상점을 개설할 수 있게 만들었습니다.


## 기술 

---
- java 17
- Spring boot 3.2
- spring security 6.2
- spring JPA,queryDsl
- thymeleaf 



## 배포 

---

AWS (Amazon web service)
- application spring boot build

OCS (oracle cloud service)
- database oracle 19c
- image repository (nginx)


## 구성 

---


### 프로젝트 구조

---

```asciidoc
|
├─── src
|     ├── docs
|     |    └─ asciidoc
│     |           ├─ domain
|     |           |    ├─ api_1~oo.adoc         # rest api  가장 작은 단위러 보통 rest api mapping 이름으로 이루어져 있음 
|     |           |    └─ index.adoc            # 중간 단계 index     
|     |           └─ index.adoc                 # 최 상위 index 이며 
|     |
|     ├── main
│     |   ├─ java
│     |   |   └─ github.jhkoder.commerce
|     |   |       ├─ security                   # security 6.x 셋팅 
|     |   |       ├─ config                     # 프로젝트 config (Jasypt,querydsl,webclient) 
|     |   |       ├─ image                      # 이미지 전송, 도메인을 관리하고 있음 
|     |   |       ├─ mail                       # 이메일 발송함
|     |   |       ├─ sms                        # 문자를 발송함
|     |   |       └─ domain                     
|     |   |           ├─ domain                 # 도메인     
|     |   |           ├─ repository             # jpa,querydsl 쿼리 패키지       
|     |   |           ├─ service                # 서비스
|     |   |           |   ├─ request            # 서비스 요청 dto 
|     |   |           |   └─ response           # 서비스 응답 dto
|     |   |           └─ web
|     |   |               ├─ rest               # rest api 통신
|     |   |               └─ view               # uri 페이지 매핑 
|     |   |
│     |   └─ resources
|     | 
|     └── test
|          ├─ java
│          |    ├─ fixture                      # 저장된 도메인 값을 반환함
│          |    └─ github.jhkoder.commerce
│          |          ├─ common
│          |          |   ├── auth              # rest api 테스트 에서 상용될 권한 주입 어노테이션
│          |          |   ├── error             # rest doc 문서에서 자동으로 error code 를 넣기 위해 사용됨 
│          |          |   ├── ouput             # rest doc 가장 작은 단위 .adoc을 만들어줌 
│          |          |   └── RestDocController # rest doc 의 공통 부분을 분리 & 모든 api 테스트가 상속 받음 
│          |          └─ domain                 
│          |              ├── rest              #  rest api 테스트로 rest doc 문서를 만들 수 있음 
│          |              └── service           #  기능 테스트
|          └─ resources
|               └─ org.springframework.restdocs # rest doc api .snippet 을 커스텀 함 
|               └─ static.img                   # 테스트 용 img
├─ build.gradle                                 # spring boot 셋팅 
└─.editconfig                                   # ide 코드 컨벤션 자동화 
```

## DB 

```application(dev,prod).yml``` 파일 에서 설정 하며
TNS 를 사용하여 연결정보를 관리합니다.


---

| | |
|--|-|
| | |



### ERD
![erd_v2.png](/md/erd_v2.png)


---

## HTTP 

---

| URI     | 설명       |
|---------|----------|
| /       | 메인 페이지   |
| /login  | 로그인 페이지  |
| /signup | 회원가입 페이지 |
| /mypage | 마이 페이지   |
| /store  | 상점 페이지   |
| /store/seller  | 판매자 전용 상점 페이지   |


[REST API docs](/md/rest_doc_sample.html)  


## 모델

---


ORM 을 사용하며 도메인으로 분리되어 있으며 클린 아키텍처 구조로 되어있습니다. 

> 사실 나는 나쁜 프로그래머와 좋은 프로그래머의 차이는 그가 자신의 코드를 더 중요하게 생각하는지 아니면 데이터 구조를
> 더 중요하게 생각하는지에 달려 있다고 주장할 것입니다. 나쁜 프로그래머는 코드에 대해 걱정합니다. 좋은 프로그래머는
> 데이터 구조와 그 관계에 대해 걱정합니다.
> 
> ── 리눅스 창시자 리누스 토볼즈(Linus Torvolds)

### 공통 부분 

- BasicEntity 을 상속 받아 추가,수정 일자와 삭제 여부 값을 가지고 있습니다. 
- Oracle 에서 boolean 타입 을 사용하지 못하므로 enum 으로 대처하여 boolean 타입처럼 사용되고 있습니다.
- ID(Long) 는 필수 기본 키 이며 자동으로 증가합니다.


## 서비스

---


- 컨트롤에서 받는 값은 기본 타입 or 도메인 or 서비스 request,response 으로 되어 있어   
- 

## 컨트롤

---

### 요청 응답

## 테스트

---

단위 테스트 로 구성 되며 Junit, Mock 을 사용합니다. 

가장 작은 단위인 도메인 테스트는 제외 되었습니다.

--- 

- Controller 
  - rest doc 
- Service 
