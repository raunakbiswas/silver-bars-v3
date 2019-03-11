import com.silverBarsMarketplace.*;
import org.junit.Test;
import tec.units.ri.quantity.Quantities;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static tec.units.ri.unit.Units.*;

public class LiveOrderBoardTest {

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderUserIdNull() {
        new Order(null, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderUserIdEmpty() {
        new Order("   ", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderQuantityNull() {
        new Order("userId", null, new Price(Currency.getInstance("GBP"), 306), OrderType.SELL);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderPriceNull() {
        new Order("userId", Quantities.getQuantity(3.5, KILOGRAM), null, OrderType.SELL);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderTypeNull() {
        new Order("userId", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), null);
    }

    @Test
    public void testValidOrder() {
        Order order = new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL);
        assert "user1".equals(order.getUserId());
        assert "3.5 kg".equals(order.getQuantity().toString());
        assert "£306".equals(order.getPrice().toString());
        assert "SELL".equals(order.getType().toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderSummaryTypeNull() {
        new OrderSummary(null, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderSummaryQuantityNull() {
        new OrderSummary(OrderType.SELL, null, new Price(Currency.getInstance("GBP"), 306));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidOrderSummaryPriceNull() {
        new OrderSummary( OrderType.BUY, Quantities.getQuantity(3.5, KILOGRAM), null);
    }

    @Test
    public void testValidOrderSummary() {
        OrderSummary orderSummary = new OrderSummary(OrderType.SELL, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306));
        assert "3.5 kg".equals(orderSummary.getQuantity().toString());
        assert "£306".equals(orderSummary.getPrice().toString());
        assert "SELL".equals(orderSummary.getType().toString());
    }

    @Test
    public void testSellOrder() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));

        List<OrderSummary> sellOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        assert sellOrdersSummary.size() == 1;
        List<OrderSummary> expectedSummary = new ArrayList<>();
        expectedSummary.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        assert sellOrdersSummary.equals(expectedSummary);

        List<OrderSummary> buyOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.BUY);
        assert buyOrdersSummary.size() == 0;
    }

    @Test
    public void testBuyOrder() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.BUY));

        List<OrderSummary> sellOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        assert sellOrdersSummary.size() == 0;

        List<OrderSummary> buyOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.BUY);
        assert buyOrdersSummary.size() == 1;
        List<OrderSummary> expectedSummary = new ArrayList<>();
        expectedSummary.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        assert buyOrdersSummary.equals(expectedSummary);
    }

    @Test
    public void testBuySellOrders() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.BUY));
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));

        List<OrderSummary> sellOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        assert sellOrdersSummary.size() == 1;
        List<OrderSummary> expectedSellSummary = new ArrayList<>();
        expectedSellSummary.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        assert sellOrdersSummary.equals(expectedSellSummary);

        List<OrderSummary> buyOrdersSummary = liveOrderBoard.liveBoardSummary(OrderType.BUY);
        assert buyOrdersSummary.size() == 1;
        List<OrderSummary> expectedBuySummary = new ArrayList<>();
        expectedBuySummary.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        assert buyOrdersSummary.equals(expectedBuySummary);
    }

    @Test
    public void testLiveOrderBoard() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));
        liveOrderBoard.createOrder(new Order("user2", Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310), OrderType.SELL));
        liveOrderBoard.createOrder(new Order("user3", Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307), OrderType.SELL));
        liveOrderBoard.createOrder(new Order("user4", Quantities.getQuantity(2.0, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));

        List<OrderSummary> results = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(5.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310)));

        results.forEach(orderSummary -> System.out.println(orderSummary.toString()));
        assert results.equals(expected);
    }

    @Test
    public void testCancelSellOrder() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        Order orderToCancel = new Order("user4", Quantities.getQuantity(2.0, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL);
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.SELL));
        liveOrderBoard.createOrder(new Order("user2", Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310), OrderType.SELL));
        liveOrderBoard.createOrder(new Order("user3", Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307), OrderType.SELL));
        liveOrderBoard.createOrder(orderToCancel);

        List<OrderSummary> results = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(5.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310)));

        System.out.println("Order summary before canceling Order: "+orderToCancel.toString());
        results.forEach(orderSummary -> System.out.println(orderSummary.toString()));
        assert results.equals(expected);

        liveOrderBoard.cancelOrder(orderToCancel);

        results = liveOrderBoard.liveBoardSummary(OrderType.SELL);
        expected = new ArrayList<>();
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307)));
        expected.add(new OrderSummary(OrderType.SELL, Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310)));

        System.out.println("Order summary after canceling Order: "+orderToCancel.toString());
        results.forEach(orderSummary -> System.out.println(orderSummary.toString()));
        assert results.equals(expected);
    }

    @Test
    public void testCancelBuyOrder() {
        LiveOrderBoard liveOrderBoard = new LiveOrderBoard();
        Order orderToCancel = new Order("user4", Quantities.getQuantity(2.0, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.BUY);
        liveOrderBoard.createOrder(new Order("user1", Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306), OrderType.BUY));
        liveOrderBoard.createOrder(new Order("user2", Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310), OrderType.BUY));
        liveOrderBoard.createOrder(new Order("user3", Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307), OrderType.BUY));
        liveOrderBoard.createOrder(orderToCancel);

        List<OrderSummary> results = liveOrderBoard.liveBoardSummary(OrderType.BUY);
        List<OrderSummary> expected = new ArrayList<>();
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310)));
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307)));
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(5.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));

        System.out.println("Order summary before canceling Order: "+orderToCancel.toString());
        results.forEach(orderSummary -> System.out.println(orderSummary.toString()));
        assert results.equals(expected);

        liveOrderBoard.cancelOrder(orderToCancel);

        results = liveOrderBoard.liveBoardSummary(OrderType.BUY);
        expected = new ArrayList<>();
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(1.2, KILOGRAM), new Price(Currency.getInstance("GBP"), 310)));
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(1.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 307)));
        expected.add(new OrderSummary(OrderType.BUY, Quantities.getQuantity(3.5, KILOGRAM), new Price(Currency.getInstance("GBP"), 306)));

        System.out.println("Order summary after canceling Order: "+orderToCancel.toString());
        results.forEach(orderSummary -> System.out.println(orderSummary.toString()));
        assert results.equals(expected);
    }
}
