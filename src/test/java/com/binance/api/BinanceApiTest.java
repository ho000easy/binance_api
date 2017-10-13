package com.binance.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zy on 2017/10/13.
 */
public class BinanceApiTest {
    private static final String SYMBOL = "LTCBTC";
    private BinanceApi binanceApi;

    @Before
    public void before(){
        binanceApi = new BinanceApi("apiKey",
                "apiSecret");
    }

    @Test
    public void testLatestPrice(){
        BigDecimal price = binanceApi.getLatestPrice("LTCBTC");
        Assert.assertTrue(price.compareTo(new BigDecimal(0)) > 0);
    }

    @Test
    public void testServerTime(){
        Long timeMills = System.currentTimeMillis();
        Long serverTime = binanceApi.getServerTime();
        Assert.assertTrue(serverTime > timeMills);
    }

    @Test
    public void testDepth(){
        BinanceApi.OrderBook orderBook = binanceApi.getDepth(SYMBOL);
        Assert.assertNotNull(orderBook.getLastUpdateId());

        BinanceApi.OrderBook orderBook20 = binanceApi.getDepth(SYMBOL, 20);
        Assert.assertTrue(orderBook20.getAsks().size() == 20);
    }

    @Test
    public void testPlaceLimitOrder(){
        BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
        placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
        placeOrderRequest.setSymbol("LTCBTC");
        placeOrderRequest.setPrice(new BigDecimal("0.005"));
        placeOrderRequest.setQuantity(new BigDecimal("1"));
        BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeLimitOrder(placeOrderRequest);
        Assert.assertNotNull(placeOrderResponse);
    }

    @Test
    public void testPlaceMarketOrder(){
        BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
        placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
        placeOrderRequest.setSymbol("LTCBTC");
        placeOrderRequest.setQuantity(new BigDecimal("1"));
        BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeMarketOrder(placeOrderRequest);
        Assert.assertNotNull(placeOrderResponse);
    }

    @Test
    public void testPlaceIcebergOrder(){//iceberg order is not supported for now
        BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
        placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
        placeOrderRequest.setSymbol("LTCBTC");
        placeOrderRequest.setPrice(new BigDecimal("0.005"));
        placeOrderRequest.setQuantity(new BigDecimal("1"));
        placeOrderRequest.setIcebergQty(new BigDecimal("2"));
        try{
            binanceApi.placeLimitOrder(placeOrderRequest);
            Assert.fail("iceberg order is supported");
        }catch (BinanceApi.BinanceException ex){
            Assert.assertEquals(ex.getCode(), new Integer(-1013));
        }
    }

    @Test
    public void testQueryOrder(){
        BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
        placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
        placeOrderRequest.setSymbol("LTCBTC");
        placeOrderRequest.setPrice(new BigDecimal("0.005"));
        placeOrderRequest.setQuantity(new BigDecimal("1"));
        BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeLimitOrder(placeOrderRequest);

        // query by orderId
        BinanceApi.Order order = binanceApi.getOrder("LTCBTC", placeOrderResponse.getOrderId());
        Assert.assertEquals(order.getOrderId(), placeOrderResponse.getOrderId());
        // query by clientOrderId
        BinanceApi.Order orderClient = binanceApi.getOrder("LTCBTC", null, placeOrderResponse.getClientOrderId(), null);
        Assert.assertEquals(orderClient.getOrderId(), placeOrderResponse.getOrderId());
        // specify the recWindow
        BinanceApi.Order orderRecWindow = binanceApi.getOrder("LTCBTC", null, placeOrderResponse.getClientOrderId(), 10000L);
        Assert.assertEquals(orderRecWindow.getOrderId(), placeOrderResponse.getOrderId());

    }

    @Test
    public void testCancelOrder(){

        BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
        placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
        placeOrderRequest.setSymbol("LTCBTC");
        placeOrderRequest.setPrice(new BigDecimal("0.005"));
        placeOrderRequest.setQuantity(new BigDecimal("1"));
        BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeLimitOrder(placeOrderRequest);

        BinanceApi.CancelOrderResponse cancelOrderResponse = binanceApi.cancelOrder("LTCBTC", placeOrderResponse.getOrderId());
        Assert.assertEquals(cancelOrderResponse.getOrderId(), placeOrderResponse.getOrderId());

        BinanceApi.Order order = binanceApi.getOrder("LTCBTC", placeOrderResponse.getOrderId());
        Assert.assertEquals(order.getStatus(), BinanceApi.OrderStatus.CANCELED);
    }

    @Test
    public void testOpenOrders(){
        List<BinanceApi.Order> orders = binanceApi.openOrders("LTCBTC");
        Assert.assertNotNull(orders);
    }

    @Test
    public void testGetAccount(){
        BinanceApi.AccountInfo accountInfo = binanceApi.getAccount();
        Assert.assertNotNull(accountInfo.getCanTrade() != null);
        // specify the recWindow
        BinanceApi.AccountInfo accountInfoRecWindwo = binanceApi.getAccount(5000L);
        Assert.assertNotNull(accountInfoRecWindwo.getCanTrade() != null);
    }
}
