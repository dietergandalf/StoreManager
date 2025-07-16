package com.dietergandalf.store_manager.util;

import com.dietergandalf.store_manager.dto.CartItemDto;
import com.dietergandalf.store_manager.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {

    @Autowired
    private ProductMapper productMapper;

    public CartItemDto toDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return CartItemDto.builder()
                .cartItemId(cartItem.getCart_item_id())
                .productStock(productMapper.toStockDto(cartItem.getProductStock()))
                .quantity(cartItem.getQuantity())
                .priceAtTimeOfAdd(cartItem.getPriceAtTimeOfAdd())
                .totalPrice(cartItem.getQuantity() * cartItem.getPriceAtTimeOfAdd())
                .build();
    }

    public List<CartItemDto> toDtoList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
