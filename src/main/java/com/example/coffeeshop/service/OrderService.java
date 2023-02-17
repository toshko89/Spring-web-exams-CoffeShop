package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.model.dto.NewOrderDTO;
import com.example.coffeeshop.model.enums.CategoryEnum;
import com.example.coffeeshop.repository.OrderRepo;
import com.example.coffeeshop.session.LoggedUserSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LoggedUserSession loggedUserSession;

    public OrderService(OrderRepo orderRepo, UserService userService, CategoryService categoryService, LoggedUserSession loggedUserSession) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.categoryService = categoryService;
        this.loggedUserSession = loggedUserSession;
    }

    public void initOrderDB() {
        if (orderRepo.count() == 0) {
            User user = userService.findUserByUserId(7L);

            Order order = new Order()
                    .setEmployee(user)
                    .setCategory(categoryService.findCategoryByName(CategoryEnum.Cake))
                    .setOrderDate(LocalDateTime.now())
                    .setName("Torta")
                    .setPrice(BigDecimal.valueOf(5.50))
                    .setDescription("Shokoladova torta");

            this.orderRepo.save(order);

            Order order2 = new Order()
                    .setEmployee(user)
                    .setCategory(categoryService.findCategoryByName(CategoryEnum.Coffee))
                    .setOrderDate(LocalDateTime.now())
                    .setName("Cafe")
                    .setPrice(BigDecimal.valueOf(2.50))
                    .setDescription("Cafe with milk");

            this.orderRepo.save(order2);

            Order order3 = new Order()
                    .setEmployee(user)
                    .setCategory(categoryService.findCategoryByName(CategoryEnum.Other))
                    .setOrderDate(LocalDateTime.now())
                    .setName("Other")
                    .setPrice(BigDecimal.valueOf(15))
                    .setDescription("Other");

            this.orderRepo.save(order3);
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = this.orderRepo.findAll();
        if (orders.size() > 0) {
            orders.sort(new OrdersCountComparator());
        }
        return orders;
    }

    public void addOrder(NewOrderDTO newOrderDTO) {
        Order order = new Order()
                .setDescription(newOrderDTO.getDescription())
                .setEmployee(userService.findUserByUserId(loggedUserSession.getId()))
                .setCategory(categoryService.findCategoryByName(CategoryEnum.valueOf(newOrderDTO.getCategory())))
                .setOrderDate(LocalDateTime.now())
                .setName(newOrderDTO.getName())
                .setPrice(newOrderDTO.getPrice());
        this.orderRepo.save(order);
    }

    public void deleteOrder(Long id) {
        this.orderRepo.deleteById(id);
    }

    private static class OrdersCountComparator implements Comparator<Order> {
        @Override
        public int compare(Order u1, Order u2) {
            return Integer.compare(u2.getPrice().intValue(), u1.getPrice().intValue());
        }
    }
}
