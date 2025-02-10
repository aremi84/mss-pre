# CodiShop 프로젝트

## 개요
8개의 카테고리에서 상품을 구매하여 코디를 완성하는 서비스를 CodisShop으로 정의하고 구매에 필요한 API를 제공합니다. 또한 관리자용 API를 통해 브랜드, 카테고리 및 상품을 관리할 수 있습니다.

## 구현 범위

**1. 관리자용 API**
  - 브랜드 생성, 업데이트 및 삭제
  - 상품 생성, 업데이트 및 삭제

**2. 사용자용 API**
  - 카테고리별 최저가 상품 조회
  - 카테고리 이름으로 최저가 및 최고가 상품 조회
  - 단일 브랜드로 모든 카테고리 상품의 최저가 조회

**3. 프론트 페이지 구현**
  - Thymeleaf를 사용하여 간단한 프론트 페이지를 구현

**4. Unit Test, Integration Test 작성**

## 프로젝트 구조
```
└── com
    └── mss
        └── pre
            ├── api
            │   ├── admin     // 관리자용 API 컨트롤러
            │   └── codishop  // 사용자용 API 컨트롤러
            ├── common
            │   ├── exception
            │   └── response
            ├── domain        // 비즈니스 로직
            │   ├── brand
            │   ├── category
            │   └── product
            ├── web
            │   ├── model
            │   └── ViewController.java
            └── MssPreApplication.java
```

## ERD
![erd](https://github.com/user-attachments/assets/acfa7fb6-a140-4e30-88d5-dd8bf9df3712)
- 하나의 상품은 반드시 하나의 브랜드와 카테고리를 가진다.
- 브랜드나 카테고리가 삭제되어도 연관된 상품은 삭제되지 않는다.
- 순수한 상품 정보만 제공해야 할 경우 삭제된 브랜드와 카테고리에 대한 처리가 필요하다.(빈 값 또는 default 정보를 제공)

## API 엔드포인트

- **브랜드 API (관리자용)**
    - `POST /admin/brand`: 새로운 브랜드 생성
    - `PUT /admin/brand/{brandId}`: 기존 브랜드 업데이트
    - `DELETE /admin/brand/{brandId}`: 브랜드 삭제

- **상품 API (관리자용)**
    - `POST /admin/product`: 새로운 상품 생성
    - `PUT /admin/product/{productId}`: 기존 상품 업데이트
    - `DELETE /admin/product/{productId}`: 상품 삭제

- **상품 정보 제공 API**
    - `GET /api/codishop/category/lowest-prices`: 카테고리별 최저가 상품 조회
    - `GET /api/codishop/category/lowest-highest-price/{categoryName}`: 카테고리 이름으로 최저가 및 최고가 상품 조회
    - `GET /api/codishop/brand/lowest-price`: 단일 브랜드로 모든 카테고리 상품의 최저가 조회

## 공통 응답 모델 정의

- **정상 응답시**
  - ResponseBodyAdvice 인터페이스를 구현한 ApiResponseBodyAdvice에서 모든 컨트롤러의 응답 본문을 변환한다.
  - beforeBodyWrite 메서드를 오버라이드하여 응답 본문을 ApiResponse 객체로 래핑하여 반환한다.
``` json
  {
    "data": {
        ... // API 응답 결과
     },
    "httpStatus": "OK", // HTTP 상태 텍스트
  }
  ```
- **에러 발생시**
  - @ControllerAdvice를 사용하여 ApiExceptionHandler에서 전역 예외 처리를 정의한다.
  - @ExceptionHandler 어노테이션을 사용하여 특정 예외를 처리하고, ApiExceptionResponse 객체를 생성하여 반환한다.
``` json
  {
    "exceptionCode": "HTTP 상태 코드",
    "exceptionMessage": "에러 메세지",
    "httpStatus": "HTTP 상태 텍스트"
  }
  ```

## 카테고리별 최저가, 최고가 상품 집계 테이블 생성 및 갱신
- 초기 데이터의 양은 많지 않지만 대량의 상품이 등록되면 전체 상품을 조회해서 카테고리별 최저가, 최고가를 계산하는 것은 부담됨
- 실무에서는 베스트 상품, 최저가 상품 등의 통계성 데이터를 집계하여 제공하는 팀이 별도로 존재하고 API를 통해 데이터를 제공받는 경우가 많음
- 즉, 외부에서 데이터를 제공받는 개념으로 집계 테이블을 생성하여 최저가, 최고가 데이터를 제공받도록 한다.
- 최초 데이터 적재는 ApplicationRunner 인터페이스를 구현한 CategoryAggregateRunner에서 수행한다.
- 애플리케이션 구동 완료 시점 최초 1회에 한하여 동작하며 이후 새로운 상품이 추가되거나 기존 상품에 변경이 발생하면 이벤트 메세지를 발송하여 집계 테이블을 갱신한다.
- ApplicationEventPublisher를 사용하여 이벤트를 발행하고, ApplicationListener를 구현한 AggregateEventListener에서 이벤트를 수신하여 처리한다.

## 프론트 페이지
![front](https://github.com/user-attachments/assets/b4733943-7cce-468c-aaf7-2917170c1bd8)
- http://localhost:8080 접속하여 확인 가능
- 구현된 API 호출, 브랜드 및 상품 추가 기능을 지원함

---
## 빌드, 테스트 및 실행

### 1. 개발 환경

- Java 17
- Spring Boot 3.4.1, Spring JPA, Hibernate
- H2 데이터베이스
  - http://localhost:8080/h2-console
  - 접속 정보는 application.yml 참고
- Gradle
- Thymeleaf + jQuery + Bootstrap
- JUnit5, Mockito

### 2. 빌드

프로젝트를 빌드하려면 다음 명령어를 실행하세요.

```sh
./gradlew build
```

### 3. 테스트

유닛 테스트 및 통합 테스트를 실행하려면 다음 명령어를 실행하세요.

```sh
./gradlew test
```

### 4. 실행
애플리케이션을 실행하려면 다음 명령어를 실행하세요.

```sh
./gradlew bootRun
```
