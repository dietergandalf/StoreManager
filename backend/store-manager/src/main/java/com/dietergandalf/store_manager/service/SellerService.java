package com.dietergandalf.store_manager.service;

import com.dietergandalf.store_manager.dto.CreateProductRequestDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.dto.RegisterRequestDto;
import com.dietergandalf.store_manager.dto.SellerDto;
import com.dietergandalf.store_manager.dto.UpdateProfileRequestDto;
import com.dietergandalf.store_manager.model.Product;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import com.dietergandalf.store_manager.repository.ProductRepository;
import com.dietergandalf.store_manager.repository.ProductStockRepository;
import com.dietergandalf.store_manager.repository.SellerRepository;
import com.dietergandalf.store_manager.util.ProductMapper;
import com.dietergandalf.store_manager.util.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SellerService {
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final SellerMapper sellerMapper;
    private final ProductMapper productMapper;

    @Autowired
    public SellerService(SellerRepository sellerRepository,
                        ProductRepository productRepository,
                        ProductStockRepository productStockRepository,
                        SellerMapper sellerMapper,
                        ProductMapper productMapper) {
        this.sellerRepository = sellerRepository;
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.sellerMapper = sellerMapper;
        this.productMapper = productMapper;
    }

    public List<SellerDto> getAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();
        return sellerMapper.toDtoList(sellers);
    }

    public SellerDto getSellerById(Long id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        return seller.map(sellerMapper::toDto).orElse(null);
    }

    public SellerDto createSeller(RegisterRequestDto registerRequest) {
        if (sellerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Seller seller = sellerMapper.fromRegisterRequest(registerRequest);
        Seller savedSeller = sellerRepository.save(seller);
        return sellerMapper.toDto(savedSeller);
    }

    public SellerDto updateSeller(Long id, UpdateProfileRequestDto updateRequest) {
        Optional<Seller> optionalSeller = sellerRepository.findById(id);
        if (optionalSeller.isEmpty()) {
            return null;
        }

        Seller seller = optionalSeller.get();
        sellerMapper.updateFromDto(seller, updateRequest);
        Seller updatedSeller = sellerRepository.save(seller);
        return sellerMapper.toDto(updatedSeller);
    }

    public boolean deleteSeller(Long id) {
        if (sellerRepository.existsById(id)) {
            sellerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public ProductStockDto addProduct(Long sellerId, CreateProductRequestDto productRequest) {
        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);
        if (optionalSeller.isEmpty()) {
            throw new RuntimeException("Seller not found");
        }

        Seller seller = optionalSeller.get();

        // Create the product
        Product product = productMapper.fromCreateRequest(productRequest);
        Product savedProduct = productRepository.save(product);

        // Create product stock for this seller
        ProductStock productStock = productMapper.createProductStock(savedProduct, seller, productRequest.getInitialStock());
        ProductStock savedProductStock = productStockRepository.save(productStock);

        return productMapper.toStockDto(savedProductStock);
    }

    public List<ProductStockDto> getSellerProducts(Long sellerId) {
        List<ProductStock> productStocks = productStockRepository.findBySellerPersonId(sellerId);
        return productMapper.toStockDtoList(productStocks);
    }

    public ProductStockDto updateProductStock(Long sellerId, Long productStockId, Integer newQuantity) {
        Optional<ProductStock> optionalProductStock = productStockRepository.findById(productStockId);
        if (optionalProductStock.isEmpty()) {
            throw new RuntimeException("Product stock not found");
        }

        ProductStock productStock = optionalProductStock.get();
        
        // Verify the product belongs to this seller
        if (!productStock.getSeller().getPerson_id().equals(sellerId)) {
            throw new RuntimeException("Product does not belong to this seller");
        }

        productStock.setAmount(newQuantity);
        ProductStock updatedProductStock = productStockRepository.save(productStock);
        
        return productMapper.toStockDto(updatedProductStock);
    }

    public boolean removeProduct(Long sellerId, Long productStockId) {
        Optional<ProductStock> optionalProductStock = productStockRepository.findById(productStockId);
        if (optionalProductStock.isEmpty()) {
            return false;
        }

        ProductStock productStock = optionalProductStock.get();
        
        // Verify the product belongs to this seller
        if (!productStock.getSeller().getPerson_id().equals(sellerId)) {
            throw new RuntimeException("Product does not belong to this seller");
        }

        productStockRepository.delete(productStock);
        return true;
    }

    public ProductStockDto updateProductPrice(Long sellerId, Long productStockId, Double newPrice) {
        Optional<ProductStock> optionalProductStock = productStockRepository.findById(productStockId);
        if (optionalProductStock.isEmpty()) {
            throw new RuntimeException("Product stock not found");
        }

        ProductStock productStock = optionalProductStock.get();
        
        // Verify the product belongs to this seller
        if (!productStock.getSeller().getPerson_id().equals(sellerId)) {
            throw new RuntimeException("Product does not belong to this seller");
        }

        Product product = productStock.getProduct();
        product.setPrice(newPrice);
        productRepository.save(product);
        
        return productMapper.toStockDto(productStock);
    }
}
