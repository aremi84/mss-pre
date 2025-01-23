package com.mss.pre.domain.brand;

import com.mss.pre.api.codishop.model.response.LowestByBrandProductInfo;
import com.mss.pre.domain.brand.entity.Brand;
import com.mss.pre.domain.brand.repository.BrandRepository;
import com.mss.pre.domain.brand.service.BrandService;
import com.mss.pre.domain.category.entity.Category;
import com.mss.pre.domain.product.entity.Product;
import com.mss.pre.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    void 등록된_브랜드가_없을_때_최저가_상품_정보를_조회한다() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        LowestByBrandProductInfo result = brandService.getLowestProductInfoByBrand();

        assertNull(result);
    }

    @Test
    void 최저가_상품_정보_조회_성공() {
        Brand brand = new Brand();
        brand.setBrandName("Brand A");

        Category category = new Category();
        category.setCategoryName("Category A");

        Product product = new Product();
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(1000L);

        List<Brand> brandList = Collections.singletonList(brand);
        List<Product> productList = Collections.singletonList(product);

        when(brandRepository.findAll()).thenReturn(brandList);
        when(productRepository.findByBrandAndDeleteYnFalse(brand)).thenReturn(productList);

        LowestByBrandProductInfo result = brandService.getLowestProductInfoByBrand();

        assertNotNull(result);
        assertEquals("Brand A", result.getProductInfo().getBrandName());
        assertEquals(1000L, result.getProductInfo().getTotalPrice());
        assertEquals(1, result.getProductInfo().getCategoryInfo().size());
        assertEquals("Category A", result.getProductInfo().getCategoryInfo().get(0).getCategory());
        assertEquals(1000L, result.getProductInfo().getCategoryInfo().get(0).getPrice());
    }

    @Test
    void 동일한_카테고리_아이디를_가진_여러개의_상품이_존재할_경우_낮은_가격의_상품을_선정한다() {
        Brand brand = new Brand();
        brand.setBrandName("Brand A");

        Category category = new Category();
        category.setCategoryName("Category A");

        Product expensiveProduct = new Product();
        expensiveProduct.setBrand(brand);
        expensiveProduct.setCategory(category);
        expensiveProduct.setPrice(2000L);

        Product cheapProduct = new Product();
        cheapProduct.setBrand(brand);
        cheapProduct.setCategory(category);
        cheapProduct.setPrice(1000L);

        List<Brand> brandList = Collections.singletonList(brand);
        List<Product> productList = Arrays.asList(expensiveProduct, cheapProduct);

        when(brandRepository.findAll()).thenReturn(brandList);
        when(productRepository.findByBrandAndDeleteYnFalse(brand)).thenReturn(productList);

        LowestByBrandProductInfo result = brandService.getLowestProductInfoByBrand();

        assertNotNull(result);
        assertEquals("Brand A", result.getProductInfo().getBrandName());
        assertEquals(1000L, result.getProductInfo().getTotalPrice());
        assertEquals(1, result.getProductInfo().getCategoryInfo().size());
        assertEquals("Category A", result.getProductInfo().getCategoryInfo().get(0).getCategory());
        assertEquals(1000L, result.getProductInfo().getCategoryInfo().get(0).getPrice());
    }
}