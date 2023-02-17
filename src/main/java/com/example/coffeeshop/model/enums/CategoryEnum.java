package com.example.coffeeshop.model.enums;

public enum CategoryEnum {
    Coffee(2), Cake(10), Drink(1), Other(5);

    private int value;

    CategoryEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
