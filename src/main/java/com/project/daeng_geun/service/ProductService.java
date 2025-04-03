package com.project.daeng_geun.service;

import com.project.daeng_geun.dto.ProductDTO;
import com.project.daeng_geun.entity.Product;
import com.project.daeng_geun.entity.User;
import com.project.daeng_geun.repository.MarketCommentRepository;
import com.project.daeng_geun.repository.ProductRepository;
import com.project.daeng_geun.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final MarketCommentRepository marketCommentRepository;

    // 상품 등록 (이미지 업로드 포함)
    @Transactional
    public ProductDTO createProduct(ProductDTO productDto, MultipartFile file) throws IOException {
        String imageUrl = (file != null) ? s3Service.uploadFile(file) : null;
        User seller = userRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new RuntimeException("판매자 정보를 찾을 수 없습니다."));

        log.info("🔥 상품 등록 요청: sellerId={}, title={}", productDto.getSellerId(), productDto.getTitle());

        Product product = Product.builder()
                .title(productDto.getTitle())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .image(imageUrl)
                .views(productDto.getViews())
                .seller(seller)
                .location(seller.getLocation())
                .createdAt(productDto.getCreatedAt())
                .build();

        return ProductDTO.fromEntity(productRepository.save(product));
    }

    // 전체 상품 목록 조회
    @Transactional(readOnly = true)
    public  List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 상품 조회 (ID 기준)
    @Transactional
    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        if (!product.isViewedRecently()) { // ✅ 최근에 조회된 상품인지 확인
            product.setViews(product.getViews() + 1);
            product.setLastViewedTime(System.currentTimeMillis()); // 마지막 조회 시간 저장
            productRepository.save(product);
        }

        return ProductDTO.fromEntity(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        if (!product.getSeller().getId().equals(userId)) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }

        productRepository.delete(product); // cascade = REMOVE에 의해 댓글도 삭제됨
    }

    // 상품 수정
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDto, Long userId, MultipartFile file) throws IOException {
        // 1상품 존재 여부 확인
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다."));

        // 로그인한 사용자가 해당 상품의 판매자인지 확인
        if (!product.getSeller().getId().equals(userId)) {
            throw new AccessDeniedException("상품을 수정할 권한이 없습니다.");
        }

        // 필수 값 검증
        if (productDto.getTitle() == null || productDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("상품 제목은 필수 입력 사항입니다.");
        }
        if (productDto.getPrice() == null || productDto.getPrice() < 0) {
            throw new IllegalArgumentException("상품 가격은 0원 이상이어야 합니다.");
        }

        // 이미지 업로드 (새 이미지가 있으면 업데이트, 없으면 기존 이미지 유지)
        String imageUrl = product.getImage(); // 기존 이미지 유지
        if (file != null && !file.isEmpty()) {
            imageUrl = s3Service.uploadFile(file);
        }

        // 상품 정보 업데이트
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImage(imageUrl); // ✅ 이미지 업데이트
        product.setLocation(productDto.getLocation());
        product.setUpdatedAt(LocalDateTime.now()); // ✅ 업데이트 시간 기록

        //  수정된 상품 저장
        productRepository.save(product);

        //  업데이트된 상품 DTO 반환
        return ProductDTO.fromEntity(product);
    }



}
