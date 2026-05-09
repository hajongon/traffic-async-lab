package com.example.after.api.product;

import com.example.after.api.stock.StockService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductRepository productRepository;
    private final StockService stockService;

    public ProductController(ProductRepository productRepository, StockService stockService) {
        this.productRepository = productRepository;
        this.stockService = stockService;
    }

    @GetMapping("/products")
    public List<ProductResponse> products() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/products/{productId}")
    public ProductResponse product(@PathVariable Long productId) {
        return toResponse(productRepository.findById(productId).orElseThrow());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), stockService.get(product.getId()));
    }
}
