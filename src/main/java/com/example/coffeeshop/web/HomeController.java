package com.example.coffeeshop.web;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.User;
import com.example.coffeeshop.model.enums.CategoryEnum;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.UserService;
import com.example.coffeeshop.session.LoggedUserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final LoggedUserSession loggedUserSession;
    private final UserService userService;
    private final OrderService orderService;

    public HomeController(LoggedUserSession loggedUserSession, UserService userService, OrderService orderService) {
        this.loggedUserSession = loggedUserSession;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {

        if (!loggedUserSession.isLoggeinIn()) {
            return "redirect:/login";
        }

        List<User> allUsers = this.userService.getAllUsers();
        List<Order> allOrders = this.orderService.getAllOrders();
        List<Order> coffee = allOrders.stream()
                .filter(order -> order.getCategory().getName().equals(CategoryEnum.Coffee)).toList();
        List<Order> cake = allOrders.stream()
                .filter(order -> order.getCategory().getName().equals(CategoryEnum.Cake)).toList();
        List<Order> drink = allOrders.stream()
                .filter(order -> order.getCategory().getName().equals(CategoryEnum.Drink)).toList();
        List<Order> other = allOrders.stream()
                .filter(order -> order.getCategory().getName().equals(CategoryEnum.Other)).toList();
        int totalTime = allOrders.stream().mapToInt(order -> order.getCategory().getNeededTime()).sum();

        model.addAttribute("allUsers", allUsers);
        model.addAttribute("coffee", coffee);
        model.addAttribute("cake", cake);
        model.addAttribute("drink", drink);
        model.addAttribute("other", other);
        model.addAttribute("allOrders", allOrders);
        for (Order allOrder : allOrders) {
            System.out.println(allOrder.getCategory().getName().toString().toLowerCase());
        }
        model.addAttribute("totalTime", totalTime);
        return "home";
    }

}
