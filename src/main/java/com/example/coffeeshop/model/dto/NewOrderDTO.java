package com.example.coffeeshop.model.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NewOrderDTO {
    @NotBlank
    @Size(min = 3,max = 20 ,message = "Name must be between 3 and 20 characters long")
    private String name;

    @NotNull
    @Min(value = 0, message = "Price cannot be negative number")
    private BigDecimal price;

    @NotNull
    @PastOrPresent
    private LocalDateTime orderTime;

    @NotBlank
    private String category;

    @NotBlank
    @Size(min = 5, message = "Description must be at least 5 characters long")
    private String description;

    public NewOrderDTO() {
    }

    public String getName() {
        return name;
    }

    public NewOrderDTO setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public NewOrderDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public NewOrderDTO setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public NewOrderDTO setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public NewOrderDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
