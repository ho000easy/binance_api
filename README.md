# Java Binance API

# Overview
A java client for binance REST api.For more information on the API and parameters for requests visit [https://www.binance.com/restapipub.htm](https://www.binance.com/restapipub.html)
This client use **lombok** to help the cleanness of code. For more information about lombok please visit [https://projectlombok.org/](https://projectlombok.org/)
# Features
* Implementation of General, Market Data and Account endpoints
* No need to handle authentication and timestamp
* simple handling of logger and exception
# Maven
1. install to local repository with command
    `mvn install`
2. add maven dependency
    ```
    <dependency>
        <groupId>com.binance</groupId>
        <artifactId>binance-api</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
# Examples
#### First Init BinanceApi
```java
BinanceApi binanceApi = new BinanceApi("apiKey","apiSecret");
```

##### Get the server time
```java
Long serverTime = binanceApi.getServerTime();
```
##### Get the depth of symbol
```java
BinanceApi.OrderBook orderBook = binanceApi.getDepth("LTCBTC");
// get depth with limit number
BinanceApi.OrderBook orderBook20 = binanceApi.getDepth("LTCBTC", 20);
```

##### Get latest price of symbol
```java
BigDecimal price = binanceApi.getLatestPrice("LTCBTC");
```
##### Place limit order
```
BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
placeOrderRequest.setSymbol("LTCBTC");
placeOrderRequest.setPrice(new BigDecimal("0.005"));
placeOrderRequest.setQuantity(new BigDecimal("1"));
BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeLimitOrder(placeOrderRequest);
```

##### Place market order
```java
BinanceApi.PlaceOrderRequest placeOrderRequest = new BinanceApi.PlaceOrderRequest();
placeOrderRequest.setOrderSide(BinanceApi.OrderSide.BUY);
placeOrderRequest.setSymbol("LTCBTC");
placeOrderRequest.setQuantity(new BigDecimal("1"));
BinanceApi.PlaceOrderResponse placeOrderResponse = binanceApi.placeMarketOrder(placeOrderRequest);
```
##### Get order
```java
BinanceApi.Order order = binanceApi.getOrder("LTCBTC", 123456L);
```

##### Cancel order
```java
BinanceApi.CancelOrderResponse cancelOrderResponse = binanceApi.cancelOrder("LTCBTC", 123456L);
```

##### Get Open orders
```java
List<BinanceApi.Order> orders = binanceApi.openOrders("LTCBTC");
```

##### Get Account Info
```java
BinanceApi.AccountInfo accountInfo = binanceApi.getAccount();
// specify the recWindow
BinanceApi.AccountInfo accountInfoRecWindwo = binanceApi.getAccount(5000L);
```



