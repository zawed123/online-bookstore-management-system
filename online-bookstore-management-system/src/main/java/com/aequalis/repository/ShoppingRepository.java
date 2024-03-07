package com.aequalis.repository;

import com.aequalis.entity.Book;
import com.aequalis.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
public interface ShoppingRepository extends JpaRepository<ShoppingCart, UUID>{
}
