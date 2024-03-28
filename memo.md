# 개발노트 

### oracle db 19c 는 spring.jpa.database-platform 와 호환 되지 않음
  ex) database-platform: org.hibernate.dialect.Oracle12cDialect

이유 : Oracle 18에서는 Hibernate의 버그로 인해 Oracle 12C 버전의 상위 버전을 감지할 수 없음

결과 :
- Hibernate ORM 5.3.16 릴리스에 적용됨
- Persistence.xml의 결함으로 인해 삭제됨 

해결 방안 :
- jpa.database-platform 는 'hibernate.dialect'를 사용하여 명시적으로 지정할 필요가 없습니다(기본적으로 선택됩니다)
  