package com.example.coffeeshop.init;

import com.example.coffeeshop.service.CategoryService;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    public DBInit(UserService userService, CategoryService categoryService, OrderService orderService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderService = orderService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.userService.initUsersDB();
        this.categoryService.initCategoryDB();
        this.orderService.initOrderDB();

    }
}
