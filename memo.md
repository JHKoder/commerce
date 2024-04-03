# 개발노트 

# 이슈 
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


### oracle 처음 사용시 jpa 고려해야할점
- oracle은 boolean 타입이 없어 String 타입 선언 후 @Size(max = 1 ) 으로 설정해야한다.  

### thymeleaf 로그인 여부 확인 오류 
- 로그인 하지 않는 상태에서 authorization.expression('isAuthenticated()') 를 사용하면 
  securityConfig에 필터링 되는 상황이 발생
- 해결방안 
```groovy
  implementation 'org.thymeleaf:thymeleaf-spring6'
  implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
```
***버전에 맞게 추가***

### image 저장소 전용 서버에서 Nginx 으로 upload,load 하기
- 이미지를 가져오는 것은 성공 하였으나 이미지 업로드 하는 부분에서
  이미지 ip가 쉽게 노출이 되어 있어 악용될 가능성이 커서 spring sftp 에서 전송하기로 결정

#### 특징 
- Spring SFTP 어뎁터는 파일이 임시 이름으로 전송되고 완전히 전송되면 이름이 바뀌는 알고리즘을 사용함 


### DefaultSftpSessionFactory 사용하여 ssh 연결시 개인 키 오류  
- 에러 : 'PuTTY-User-Key-File-3: ssh-rsa': Bad format (no key data delimiter): ssh-rsa
-  ssh-key-2024-03-31': Bad format (no key data delimiter): ssh-key-2024-03-31 .. 등등 
rsa sha2 를 지원하는 Jsch 사용하여 해결 
- ###### [JCraft Jsch에서 Apache MINA로 마이그레이션]


### querydsl spring boot version 셋팅 변화 
[Spring boot 3.2 JPA, querydsl 5.0.0 ,gradle 8]

공식 설정에 대한 참고 내용

특징 
- javax -> jakarta 사용으로 변함 
  - apt-maven-plugin을 완전히 제거
  - outputDirectory를 target/generated-sources/annotations로 설정합니다. 이렇게 하면 Java 컴파일러가 Q-클래스를 해당 디렉토리에 생성하게 됩니다.
  - outputDirectory를 오버라이드하지 않습니다. 이렇게 하면 기본 설정값이 사용되어 Java 컴파일러가 Q-클래스를 적절한 위치에 생성합니다.

##### before 2.x
```groovy
dependencies {
    //querydsl
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
}
def querydslDir = "$buildDir/generated/querydsl" 
querydsl {
	jpa = true 
	querydslSourcesDir = querydslDir
}

sourceSets {
	main.java.srcDir querydslDir
}

compileQuerydsl{
	options.annotationProcessorPath = configurations.querydsl
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
	querydsl.extendsFrom compileClasspath 
}
```

##### after 3.x
```groovy

dependencies {
    //querydsl 
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
    annotationProcessor "jakarta.annotation:jakarta.annotation-api"
    annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}
```

이전 버전 사용시 나타 날 수 있는 오류
###### [Attempt to recreate a file for type ...QEntity]

################################################
---
# 참고한 자료

#### DB erd 제작시 참고한 문서 
- [네이버 쇼핑 EP](https://join.shopping.naver.com/misc/download/ep_guide.nhn)



### Jasypt 참고 자료
- [(Baeldung) 사용방법](https://www.baeldung.com/spring-boot-jasypt)
- [(공식 Github 주소) Jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot)


## 이미지 제작에 도움준 사이트
- 제작 툴 어도비 일러스트 2023
- https://coolors.co/ 
- https://getbootstrap.com/
- https://pixabay.com/ko/

## cloud image server 개설에 참고한 자료

- [(Blog)이미지 스토리지 서버 구축 및 최적화](https://tecoble.techcourse.co.kr/post/2022-09-13-image-storage-server/)

## sftp private-key 연결에 도움준 자료 (ubuntu20.04,rsa-sha2)
- https://github.com/spring-projects/spring-integration/blob/627cde2050da92f41767243c64be6d815d6b417f/spring-integration-sftp/src/test/java/org/springframework/integration/sftp/session/SftpServerTests.java#L117
- https://github.com/spring-projects/spring-integration/issues/8693

## AWS 프리 티어  대하여 (쓰는 것만)
750 한달에 한 인스턴스 등 해야 무료 요금이 적용됨
- ec2 (750)[12개월 무료]
- database RDS (750)[12개월 무료]
  - 관리형 관계형 데이터베이스
- DynamoDB 25GB [평생무료]
- 스토리지 5GB(750)[12개월]


## nginx 참고 자료
https://whatisthenext.tistory.com/123

## image upload 찾고 문서
https://docs.spring.io/spring-integration/reference/sftp.html

https://docs.spring.io/spring-integration/api/org/springframework/integration/file/remote/RemoteFileTemplate.html

[java Example code](https://docs.spring.io/spring-integration/reference/sftp/inbound.html)


## querydsl
- http://querydsl.com/ 
