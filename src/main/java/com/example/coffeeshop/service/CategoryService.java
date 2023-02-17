package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Category;
import com.example.coffeeshop.model.enums.CategoryEnum;
import com.example.coffeeshop.repository.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public void initCategoryDB() {
        if(categoryRepo.count() == 0) {
            Arrays.stream(CategoryEnum.values()).forEach(categoryEnum -> {
                Category category = new Category()
                        .setName(categoryEnum)
                        .setNeededTime(categoryEnum.getValue());

                this.categoryRepo.save(category);
            });
        }
    }

    public Category findCategoryByName(CategoryEnum categoryEnum) {
        return this.categoryRepo.findByName(categoryEnum).orElse(null);
    }
}
