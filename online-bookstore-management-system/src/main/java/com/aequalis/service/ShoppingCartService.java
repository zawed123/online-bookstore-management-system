package com.aequalis.service;

import com.aequalis.dto.CartDto;
import com.aequalis.entity.ShoppingCart;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    public List<CartDto>  addToCart(UUID id, ShoppingCart shoppingCart);
}
