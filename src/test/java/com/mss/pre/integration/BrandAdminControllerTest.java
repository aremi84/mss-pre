package com.mss.pre.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.pre.api.admin.model.request.BrandRequest;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BrandAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        brandRepository.deleteAll();
    }

    @Test
    void 새로운_브랜드를_생성한다() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("New Brand");

        mockMvc.perform(post("/admin/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Brand> brand = brandRepository.findByBrandName("New Brand");
        assertTrue(brand.isPresent());
    }

    @Test
    void 브랜드명을_업데이트한다() throws Exception {
        Brand brand = new Brand();
        brand.setBrandName("Old Brand");
        brandRepository.save(brand);

        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        mockMvc.perform(put("/admin/brand/" + brand.getBrandId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Brand> updatedBrand = brandRepository.findByBrandIdAndDeleteYnFalse(brand.getBrandId());
        assertTrue(updatedBrand.isPresent());
        assertEquals("Updated Brand", updatedBrand.get().getBrandName());
    }

    @Test
    void 브랜드를_삭제한다() throws Exception {
        Brand brand = new Brand();
        brand.setBrandName("Brand to Delete");
        brandRepository.save(brand);

        mockMvc.perform(delete("/admin/brand/" + brand.getBrandId()))
                .andExpect(status().isOk());

        Optional<Brand> deletedBrand = brandRepository.findByBrandIdAndDeleteYnFalse(brand.getBrandId());
        assertFalse(deletedBrand.isPresent());
    }
}
