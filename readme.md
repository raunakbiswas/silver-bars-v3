# Silver Bars Marketplace

This is a gradle project that provides an implementation for Live Order Board of the Silver Bars Marketplace.

## Build Project

This project requires `gradle` to be installed.
Run `gradle clean build` from project's root

### Generate jar file

Note: If you have already run `gradle build` you can locate the jar file in `silver-bars-v3/build/libs`
Run `gradle jar` from project's root. The jar file `silver-bars-v3-1.0-SNAPSHOT.jar` can be located in `silver-bars-v3/build/libs`

## Functionality

### Create Order

```java
LiveOrderBoard liveOrderBoard = new LiveOrderBoard();

liveOrderBoard.createOrder(new Order(
    "user1",
    Quantities.getQuantity(3.5, KILOGRAM),
    new Price(Currency.getInstance("GBP"), 306),
    OrderType.SELL
));
```

### Cancel an Order

```java
LiveOrderBoard liveOrderBoard = new LiveOrderBoard();

Order order = new Order(
  "user1",
  Quantities.getQuantity(3.5, KILOGRAM),
  new Price(Currency.getInstance("GBP"), 306),
  OrderType.SELL
);

liveOrderBoard.createOrder(order);
liveOrderBoard.cancelOrder(order);
```

### Live Order Board Summary

Live order board summary can be requested by given `OrderType`

```java
LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));
liveOrderBoard.createOrder(new Order("user2", Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310), OrderType.SELL));
liveOrderBoard.createOrder(new Order("user3", Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307), OrderType.SELL));
liveOrderBoard.createOrder(new Order("user4", Quantities.getQuantity(2.0, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));

List<OrderSummary> sellSummary = liveOrderBoard.liveBoardSummary(OrderType.SELL);
sellSummary.forEach(orderSummary -> System.out.println(orderSummary.toString()));
```

## Design Considerations

### Scalability

The time complexity involved in access operations on `LiveOrderBoard` is O(log n). This is not only an improvement on the time complexity of `List` based implementations but due to the nature of `TreeMap`s, the `Orders` can be pre-sorted by `Price` and therefore, there is no additional requirement to sort `OrderSummary`s when `liveBoardSummary` is requested.

### Limitations

This implementation supports multiple currencies for setting `Price` and `OrderSummary` for different currencies are treated seperately. But there is no consideration for comparing `Price`s based on currency exchange rates and is beyond the scope of this project.
