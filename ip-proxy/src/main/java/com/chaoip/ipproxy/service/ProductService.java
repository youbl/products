package com.chaoip.ipproxy.service;

import com.chaoip.ipproxy.controller.dto.ProductDto;
import com.chaoip.ipproxy.repository.ProductRepository;
import com.chaoip.ipproxy.repository.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 产品包服务类
 */
@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(ProductDto dto) {// 不使用的属性，必须要用 withIgnorePaths 忽略，否则会列入条件
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "type", "numPerDay", "numPerTime", "moneyPerUnit", "status", "creationTime");
        if (StringUtils.hasText(dto.getName())) {
            matcher = matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.exact());
        } else {
            matcher = matcher.withIgnorePaths("name");
        }
        Product condition = Product.builder()
                .name(dto.getName())
                .build();
        Example<Product> example = Example.of(condition, matcher);
        return productRepository.pageSearch(example, dto.getPageNum(), dto.getPageSize(), "creationTime", true);


    }

    public Product findById(long id) {
        return productRepository.findById(id);
    }

    public void changeStatus(long id) {
        Product product = findById(id);
        if (product == null) {
            throw new RuntimeException("指定的产品不存在:" + id);
        }
        product.setStatus(product.getStatus() == 0 ? 1 : 0);
        productRepository.save(product);
    }
}
