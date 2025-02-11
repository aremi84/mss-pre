/** 카테고리 테이블 생성 **/
DROP TABLE IF EXISTS CATEGORY;
CREATE TABLE CATEGORY (
    CATEGORY_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 아이디',
    CATEGORY_NAME VARCHAR(50) NOT NULL COMMENT '카테고리명'
);

/** 브랜드 테이블 생성 **/
DROP TABLE IF EXISTS BRAND;
CREATE TABLE BRAND (
    BRAND_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '브랜드 아이디',
    BRAND_NAME VARCHAR(50) NOT NULL COMMENT '브랜드명',
    DELETE_YN BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제여부'
);

/** 상품 테이블 생성 **/
DROP TABLE IF EXISTS PRODUCT;
CREATE TABLE PRODUCT (
    PRODUCT_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 아이디',
    BRAND_ID BIGINT NOT NULL COMMENT '브랜드 아이디',
    CATEGORY_ID BIGINT NOT NULL COMMENT '카테고리 아이디',
    PRICE BIGINT NOT NULL COMMENT '가격',
    DELETE_YN BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제여부'
);

/** 카테고리별 최저가, 최고가 집계 테이블 생성 **/
DROP TABLE IF EXISTS AGGREGATE_BY_PRICE;
CREATE TABLE AGGREGATE_BY_PRICE (
    CATEGORY_ID BIGINT PRIMARY KEY COMMENT '카테고리 아이디',
    LOWEST_PRODUCT_ID BIGINT COMMENT '최저가 상품 아이디',
    LOWEST_PRICE BIGINT NOT NULL COMMENT '최저가',
    HIGHEST_PRODUCT_ID BIGINT COMMENT '최고가 상품 아이디',
    HIGHEST_PRICE BIGINT NOT NULL COMMENT '최고가'
);

/** 브랜드 테이블 인덱스 생성 **/
CREATE INDEX idx_brand_name ON BRAND(BRAND_NAME)