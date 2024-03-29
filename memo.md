# 개발노트 

## 이슈 
### oracle db 19c 는 spring.jpa.database-platform 와 호환 되지 않음
  ex) database-platform: org.hibernate.dialect.Oracle12cDialect

이유 : Oracle 18에서는 Hibernate의 버그로 인해 Oracle 12C 버전의 상위 버전을 감지할 수 없음

- Hibernate ORM 5.3.16 릴리스에 적용됨
- Persistence.xml의 결함으로 인해 삭제됨 

해결 방안 :
- jpa.database-platform 는 'hibernate.dialect'를 사용하여 명시적으로 지정할 필요가 없습니다(기본적으로 선택됩니다)

### spring boot 3.2 security 변경점
[@EnableWebSecurity]
- http.csrf() Deprecated
  - .csrf(AbstractHttpConfigurer::disable)
- and() 제거
  - 람다식 사용
- User.withDefaultPasswordEncoder() 더 이상 사용 되지 않음
  - "password"가 소스 코드로 컴파일된 다음 생성 시 메모리에 포함되기 때문에 프로덕션에서는 안전하지 않습니다


- [공식 문서 ](https://docs.spring.io/spring-security/site/docs/4.0.0.RC1/reference/html/new.html)



---
## 참고 자료
#### e-commerce erd 제작시 참고한 자료
- [네이버 쇼핑 EP](https://join.shopping.naver.com/misc/download/ep_guide.nhn)

#### . 

### oracle 처음 사용시 jpa 고려해야할점
- ㄹㅇ