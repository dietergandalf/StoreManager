package com.dietergandalf.store_manager.repository;

import com.dietergandalf.store_manager.model.ProductStock;
import com.dietergandalf.store_manager.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    List<ProductStock> findBySeller(Seller seller);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.seller.person_id = :sellerId")
    List<ProductStock> findBySellerPersonId(@Param("sellerId") Long sellerId);
    
    List<ProductStock> findByAmountGreaterThan(Integer minStock);
}
