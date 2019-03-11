package com.silverBarsMarketplace;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

/**
 * The Order class represents a silver bar order
 */

public class Order {
    private String userId;
    private Quantity<Mass> quantity;
    private Price price;
    private OrderType type;

    public Order (String userId, Quantity<Mass> quantity, Price price, OrderType type) {
        validate(userId, quantity, price, type);
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getUserId() {
        return this.userId;
    }

    public Quantity<Mass> getQuantity() {
        return this.quantity;
    }

    public Price getPrice() {
        return this.price;
    }

    public OrderType getType() {
        return this.type;
    }

    public String toString() {
        return String.format("Order: {userId: %s, quantity: %s, price: %s, type: %s}", userId, quantity.toString(), price.toString(), type.toString());
    }

    private void validate(String userId, Quantity<Mass> quantity, Price price, OrderType type) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("parameter 'userId' of Order must not be null or empty");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("parameter 'quantity' of Order must not be null");
        }

        if (price == null) {
            throw new IllegalArgumentException("parameter 'price' of Order must not be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("parameter 'type' of Order must not be null");
        }
    }
}