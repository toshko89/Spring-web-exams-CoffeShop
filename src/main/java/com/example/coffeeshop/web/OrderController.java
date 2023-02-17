package com.example.coffeeshop.web;

import com.example.coffeeshop.model.dto.NewOrderDTO;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.session.LoggedUserSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {

    private final LoggedUserSession loggedUserSession;
    private final OrderService orderService;

    public OrderController(LoggedUserSession loggedUserSession, OrderService orderService) {
        this.loggedUserSession = loggedUserSession;
        this.orderService = orderService;
    }

    @ModelAttribute
    public NewOrderDTO initNewOrderDTO() {
        return new NewOrderDTO();
    }

    @GetMapping("/add-order")
    public String addOrder() {
        if (!loggedUserSession.isLoggeinIn()) {
            return "redirect:/login";
        }
        return "add-order";
    }

    @PostMapping("/add-order")
    public String addOrder(@Valid NewOrderDTO newOrderDTO,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!loggedUserSession.isLoggeinIn()) {
            return "redirect:/login";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("newOrderDTO", newOrderDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.newOrderDTO", bindingResult);
            return "redirect:/add-order";
        }

        this.orderService.addOrder(newOrderDTO);

        return "redirect:/home";
    }

    @GetMapping("/ready/{id}")
    public String readyOrder(@PathVariable Long id) {
        if (!loggedUserSession.isLoggeinIn()) {
            return "redirect:/login";
        }

        this.orderService.deleteOrder(id);

        return "redirect:/home";
    }
}
