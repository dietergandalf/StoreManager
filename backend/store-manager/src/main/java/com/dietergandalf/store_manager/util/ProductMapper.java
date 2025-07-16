package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.CreateProductRequestDto;
import com.dietergandalf.store_manager.dto.ProductDto;
import com.dietergandalf.store_manager.dto.ProductStockDto;
import com.dietergandalf.store_manager.model.Product;
import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDto.builder()
                .productId(product.getProduct_id())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductStockDto toStockDto(ProductStock productStock) {
        if (productStock == null) {
            return null;
        }

        return ProductStockDto.builder()
                .productStockId(productStock.getProduct_stock_id())
                .product(toDto(productStock.getProduct()))
                .sellerId(productStock.getSeller() != null ? productStock.getSeller().getPerson_id() : null)
                .sellerName(productStock.getSeller() != null ? 
                    productStock.getSeller().getFirst_name() + " " + productStock.getSeller().getLast_name() : null)
                .amount(productStock.getAmount())
                .build();
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductStockDto> toStockDtoList(List<ProductStock> productStocks) {
        return productStocks.stream()
                .map(this::toStockDto)
                .collect(Collectors.toList());
    }

    public Product fromCreateRequest(CreateProductRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        return product;
    }

    public ProductStock createProductStock(Product product, Seller seller, Integer initialStock) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setSeller(seller);
        productStock.setAmount(initialStock != null ? initialStock : 0);

        return productStock;
    }
}
