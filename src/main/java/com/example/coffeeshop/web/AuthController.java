package com.example.coffeeshop.web;

import com.example.coffeeshop.model.dto.UserLoginDTO;
import com.example.coffeeshop.model.dto.UserRegisterDTO;
import com.example.coffeeshop.service.UserService;
import com.example.coffeeshop.session.LoggedUserSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final LoggedUserSession loggedUserSession;

    @Autowired
    public AuthController(UserService userService,
                          LoggedUserSession loggedUserSession) {
        this.userService = userService;
        this.loggedUserSession = loggedUserSession;
    }

    @ModelAttribute
    public UserRegisterDTO initRegisterDTO() {
        return new UserRegisterDTO();
    }

    @ModelAttribute
    public UserLoginDTO initLoginDTO() {
        return new UserLoginDTO();
    }

    @GetMapping("/register")
    public String register() {
        if (loggedUserSession.isLoggeinIn()) {
            return "redirect:/home";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterDTO userRegisterDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (loggedUserSession.isLoggeinIn()) {
            return "redirect:/home";
        }

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            bindingResult.addError(new FieldError(
                    "differentConfirmPassword", "confirmPassword", "Passwords dont match"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);

            return "redirect:/register";
        }

        this.userService.register(userRegisterDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        if (loggedUserSession.isLoggeinIn()) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDTO userLoginDTO, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (loggedUserSession.isLoggeinIn()) {
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userLoginDTO", userLoginDTO);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDTO", bindingResult);

            return "redirect:/login";
        }

        boolean validCredentials = this.userService.checkUserData(userLoginDTO);

        if (!validCredentials) {
            redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
            redirectAttributes.addFlashAttribute("validCredentials", false);

            return "redirect:/login";
        }

        this.userService.login(userLoginDTO);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        if (!loggedUserSession.isLoggeinIn()) {
            return "redirect:/login";
        }
        this.userService.logout();

        return "redirect:/";
    }
}
