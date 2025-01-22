package com.mss.pre.integration;

import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import com.mss.pre.domain.category.component.CategoryAggregateRunner;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.repository.CategoryAggregateRepository;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CodiShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryAggregateRepository categoryAggregateRepository;

    @Autowired
    private CategoryAggregateRunner categoryAggregateRunner;

    private Brand brand;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();
        categoryAggregateRepository.deleteAll();

        brand = new Brand();
        brand.setBrandName("Test Brand");
        brandRepository.save(brand);

        category = new Category();
        category.setCategoryName("Test Category");
        categoryRepository.save(category);

        product = new Product();
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(1000L);
        productRepository.save(product);

        categoryAggregateRunner.run(null);
    }

    @Test
    void 카테고리별_최저가격_브랜드와_상품_가격_총액을_조회한다() throws Exception {
        mockMvc.perform(get("/api/codishop/category/lowest-prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.최저가상품[0].브랜드").value("Test Brand"))
                .andExpect(jsonPath("$.data.최저가상품[0].카테고리").value("Test Category"))
                .andExpect(jsonPath("$.data.최저가상품[0].가격").value(1000))
                .andExpect(jsonPath("$.data.총합").value(1000));
    }

    @Test
    void 카테고리_이름으로_최저_최고_가격_브랜드와_상품_가격을_조회한다() throws Exception {
        mockMvc.perform(get("/api/codishop/category/lowest-highest-price/Test Category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.카테고리").value("Test Category"))
                .andExpect(jsonPath("$.data.최저가.브랜드").value("Test Brand"))
                .andExpect(jsonPath("$.data.최저가.가격").value(1000))
                .andExpect(jsonPath("$.data.최고가.브랜드").value("Test Brand"))
                .andExpect(jsonPath("$.data.최고가.가격").value(1000));

    }

    @Test
    void 단일_브랜드로_모든_카테고리_상품을_구매할_때_최저가격에_판매하는_브랜드와_카테고리의_상품가격_총액을_조회한다() throws Exception {
        mockMvc.perform(get("/api/codishop/brand/lowest-price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.최저가.브랜드").value("Test Brand"))
                .andExpect(jsonPath("$.data.최저가.카테고리[0].카테고리").value("Test Category"))
                .andExpect(jsonPath("$.data.최저가.카테고리[0].가격").value(1000))
                .andExpect(jsonPath("$.data.최저가.총액").value(1000));
    }

    @Test
    void 카테고리_정보를_조회한다() throws Exception {
        mockMvc.perform(get("/api/codishop/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].categoryName").value("Test Category"));
    }

    @Test
    void 브랜드_정보를_조회한다() throws Exception {
        mockMvc.perform(get("/api/codishop/brand")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].brandName").value("Test Brand"));
    }
}