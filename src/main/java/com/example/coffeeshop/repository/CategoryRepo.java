package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Category;
import com.example.coffeeshop.model.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> findByName(CategoryEnum categoryEnum);
}
