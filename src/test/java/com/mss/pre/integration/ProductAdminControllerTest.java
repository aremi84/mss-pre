package com.mss.pre.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.pre.api.admin.model.request.ProductRequest;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.category.repository.CategoryRepository;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();

        brand = new Brand();
        brand.setBrandName("Test Brand");
        brandRepository.save(brand);

        category = new Category();
        category.setCategoryName("Test Category");
        categoryRepository.save(category);
    }

    @Test
    void 새로운_상품을_생성한다() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setBrandId(brand.getBrandId());
        request.setCategoryId(category.getCategoryId());
        request.setPrice(1000L);

        mockMvc.perform(post("/admin/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Product> product = productRepository.findAll().stream().findFirst();
        assertTrue(product.isPresent());
    }

    @Test
    void 상품을_업데이트한다() throws Exception {
        Product product = new Product();
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(1000L);
        productRepository.save(product);

        ProductRequest request = new ProductRequest();
        request.setBrandId(brand.getBrandId());
        request.setCategoryId(category.getCategoryId());
        request.setPrice(2000L);

        mockMvc.perform(put("/admin/product/" + product.getProductId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Product> updatedProduct = productRepository.findById(product.getProductId());
        assertTrue(updatedProduct.isPresent());
        assertEquals(2000L, updatedProduct.get().getPrice());
    }

    @Test
    void 상품을_삭제한다() throws Exception {
        Product product = new Product();
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(1000L);
        productRepository.save(product);

        mockMvc.perform(delete("/admin/product/" + product.getProductId()))
                .andExpect(status().isOk());

        Optional<Product> deletedProduct = productRepository.findById(product.getProductId());
        assertTrue(deletedProduct.isPresent() && deletedProduct.get().isDeleteYn());
    }
}