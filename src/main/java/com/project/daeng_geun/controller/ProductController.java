package com.project.daeng_geun.controller;

import com.project.daeng_geun.dto.ProductDTO;
import com.project.daeng_geun.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // React와의 CORS 문제 해결
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. 상품 등록

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") ProductDTO productDto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        return ResponseEntity.ok(productService.createProduct(productDto, image));
    }

    // 2. 전체 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        System.out.println("👉 Product ID 요청됨: " + productId); // 디버깅 로그
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("productId") Long productId,
            @RequestParam Long userId,
            @RequestParam("title") String title,
            @RequestParam("price") Integer price,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException { // ✅ 파일 추가

        ProductDTO productDto = new ProductDTO();
        productDto.setTitle(title);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setLocation(location);

        // Service 메서드 호출 시 MultipartFile 추가
        ProductDTO updatedProduct = productService.updateProduct(productId, productDto, userId, imageFile);

        return ResponseEntity.ok(updatedProduct);
    }

    // 5. 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId,
            @RequestParam Long userId) {

        productService.deleteProduct(productId, userId);
        return ResponseEntity.ok().build();
    }

}
