package com.silverBarsMarketplace;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;
import java.util.Objects;

/**
 * The OrderSummary class represents an order summary based on Price and OrderType with an aggregate quantity
 */

public class OrderSummary {
    private final OrderType type;
    private final Quantity<Mass> quantity;
    private final Price price;

    public OrderSummary(OrderType type, Quantity<Mass> quantity, Price price) {
        validate(type, quantity, price);
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderType getType() {
        return type;
    }

    public Quantity<Mass> getQuantity() {
        return quantity;
    }

    public Price getPrice() {
        return price;
    }

    public String toString() {
        return String.format("%s: %s for %s", type, quantity, price);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        OrderSummary orderSummary = (OrderSummary) object;

        return type == orderSummary.getType() &&
                quantity.getUnit().equals(orderSummary.getQuantity().getUnit()) &&
                quantity.getValue().equals(orderSummary.getQuantity().getValue()) &&
                price.compareTo(orderSummary.getPrice()) == 0;
    }

    public int hashCode() {
        return Objects.hash(this.type, this.quantity, this.price);
    }

    private void validate(OrderType type, Quantity<Mass> quantity, Price price) {
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
