package com.dietergandalf.store_manager.repository;

import com.dietergandalf.store_manager.model.CartItem;
import com.dietergandalf.store_manager.model.ShoppingCart;
import com.dietergandalf.store_manager.model.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(ShoppingCart cart);
    Optional<CartItem> findByCartAndProductStock(ShoppingCart cart, ProductStock productStock);
    void deleteByCart(ShoppingCart cart);
}
