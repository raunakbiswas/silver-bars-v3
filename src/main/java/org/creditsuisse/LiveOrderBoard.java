package org.creditsuisse;

import tec.units.ri.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;
import java.util.*;

import static tec.units.ri.unit.Units.KILOGRAM;

/**
 * The LiveOrderBoard class is used to manage silver bar orders.
 * This class maintains to seperate TreeMaps to manage BUY and SELL orders based on Price.
 * The use of TreeMaps is aimed at pre-sorting BUY and SELL orders as per specification of the Live Order Board,
 * which requires BUY orders to be sorted by descending Price value and opposite for SELL orders.
 *
 * This class includes methods for creating, canceling orders and a summary method that returns a
 * List of OrderSummary based on the OrderType.
 */

public class LiveOrderBoard {
    private TreeMap<Price, List<Order>> sellOrders = new TreeMap<>();
    private TreeMap<Price, List<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());

    public Order createOrder(Order order) {

        if (order.getType() == OrderType.SELL) {
            List<Order> ordersList = sellOrders.get(order.getPrice());
            if (ordersList == null) {
                ordersList = new ArrayList<>();
            }
            ordersList.add(order);
            sellOrders.put(order.getPrice(), ordersList);
        } else {
            List<Order> ordersList = buyOrders.get(order.getPrice());
            if (ordersList == null) {
                ordersList = new ArrayList<>();
            }
            ordersList.add(order);
            buyOrders.put(order.getPrice(), ordersList);
        }

        return order;
    }

    public void cancelOrder(Order order) {
        if (order == null) {
            return;
        }

        if (order.getType() == OrderType.SELL) {
            sellOrders.get(order.getPrice()).remove(order);
        } else {
            buyOrders.get(order.getPrice()).remove(order);
        }

    }

    public List<OrderSummary> liveBoardSummary(OrderType type) {
        List<OrderSummary> results = new ArrayList<>();
        switch (type) {
            case SELL:
                for (Map.Entry<Price, List<Order>> entry: sellOrders.entrySet()) {
                    Quantity<Mass> total = entry.getValue()
                                                .stream()
                                                .map(s -> s.getQuantity())
                                                .reduce(Quantities.getQuantity(0.0, KILOGRAM), (agg, mass) -> agg.add(mass));
                    results.add(new OrderSummary(type, total, entry.getKey()));
                }
                break;
            case BUY:
                for (Map.Entry<Price, List<Order>> entry: buyOrders.entrySet()) {
                    Quantity<Mass> total = entry.getValue()
                                                .stream()
                                                .map(s -> s.getQuantity())
                                                .reduce(Quantities.getQuantity(0.0, KILOGRAM), (agg, mass) -> agg.add(mass));

                    results.add(new OrderSummary(type, total, entry.getKey()));
                }
                break;
        }
        return results;
    }
}
