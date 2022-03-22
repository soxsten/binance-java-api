package com.binance.api.client.impl;

import com.binance.api.Properties;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderStatus;
import com.binance.api.client.domain.TimeInForce;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.OrderList;
import com.binance.api.client.domain.account.TradeHistoryItem;
import com.binance.api.client.domain.account.request.AllOrderListRequest;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.CancelOrderResponse;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.market.OrderBook;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.domain.market.TickerStatistics;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ruslan Lagoiko
 */
public class BinanceApiRestClientImplTest {

    public static final String LTCUSDT = "LTCUSDT";
    BinanceApiRestClient restClient;

    @Before
    public void setUp() throws Exception {
        Properties.load();
        restClient = Properties.getRestClient();
    }

    @Test
    public void ping() {
        restClient.ping();
    }

    @Test
    public void getServerTime() {
        restClient.getServerTime();
    }

    @Test
    public void getExchangeInfo() {
        restClient.getExchangeInfo();
    }

    @Test
    public void getAllAssets() {
        final List<Asset> allAssets = restClient.getAllAssets();
        System.out.println();
    }

    @Test
    public void getOrderBook() {
        final OrderBook orderBook = restClient.getOrderBook(LTCUSDT, 100);
    }

    @Test
    public void getTrades() {
        final List<TradeHistoryItem> trades = restClient.getTrades(LTCUSDT, 500);
    }

    @Test
    public void getHistoricalTrades() {
//        restClient.getHistoricalTrades(LTCUSDT, 500, );
    }

    @Test
    public void getAggTrades() {
    }

    @Test
    public void testGetAggTrades() {
    }

    @Test
    public void getCandlestickBars() {
    }

    @Test
    public void testGetCandlestickBars() {
    }

    @Test
    public void get24HrPriceStatistics() {
        final TickerStatistics statistics = restClient.get24HrPriceStatistics(LTCUSDT);
    }

    @Test
    public void getAll24HrPriceStatistics() {
    }

    @Test
    public void getPrice() {
        final TickerPrice btc = restClient.getPrice(LTCUSDT);
        System.out.println();
    }

    @Test
    public void getAllPrices() {
        final List<TickerPrice> allPrices = restClient.getAllPrices();
    }

    @Test
    public void getBookTickers() {
        restClient.getBookTickers();
    }

    @Test
    public void newOrderBuyLimitGTC() {
//        given
        final NewOrder order = NewOrder.limitBuy(LTCUSDT, TimeInForce.GTC, "5", "100");
        final OrderRequest orderRequest = new OrderRequest("LTCUSDT");
        final List<Order> initQuantity = restClient.getOpenOrders(orderRequest);

//        when
        final NewOrderResponse newOrderResponse = restClient.newOrder(order);

//        then
        final List<Order> actualQuantity = restClient.getOpenOrders(orderRequest);
        assertEquals(initQuantity.size() + 1, actualQuantity.size());
    }

    @Test
    public void newOrderSellLimitGTC() {
//        given
        final NewOrder order = NewOrder.limitSell(LTCUSDT, TimeInForce.GTC, "1", "500");
        final OrderRequest orderRequest = new OrderRequest(LTCUSDT);
        final List<Order> initQuantity = restClient.getOpenOrders(orderRequest);
        final HashSet<OrderStatus> allowStatuses = new HashSet<>();
        allowStatuses.add(OrderStatus.FILLED);
        allowStatuses.add(OrderStatus.NEW);

//        when
        final NewOrderResponse newOrderResponse = restClient.newOrder(order);

//        then
        final List<Order> actualQuantity = restClient.getOpenOrders(orderRequest);
        assertEquals(initQuantity.size() + 1, actualQuantity.size());
        assertTrue(allowStatuses.contains(newOrderResponse.getStatus()));
    }

    @Test
    public void newOrderTest() {
    }

    @Test
    public void getOrderStatus() {
        final List<Order> openOrders = restClient.getOpenOrders(new OrderRequest(LTCUSDT));
        final String clientOrderId = openOrders.get(0).getClientOrderId();
        final Long orderId = openOrders.get(0).getOrderId();
        final Order orderStatus = restClient.getOrderStatus(new OrderStatusRequest(LTCUSDT, clientOrderId));
        final Order orderStatus1 = restClient.getOrderStatus(new OrderStatusRequest(LTCUSDT, orderId));
        System.out.println();
        assertEquals(OrderStatus.NEW, orderStatus.getStatus());
        assertEquals(OrderStatus.NEW, orderStatus1.getStatus());
    }

    @Test
    public void cancelOrder() {
        final NewOrder order = NewOrder.limitSell(LTCUSDT, TimeInForce.GTC, "1", "500");
        final NewOrderResponse response = restClient.newOrder(order);
        final CancelOrderRequest request = new CancelOrderRequest(LTCUSDT, response.getClientOrderId());
        final CancelOrderResponse cancelOrder = restClient.cancelOrder(request);
        assertEquals(OrderStatus.CANCELED, cancelOrder.getStatus());
    }

    @Test
    public void getOpenOrders() {
    }

    @Test
    public void getAllOrders() {
        final AllOrdersRequest orderRequest = new AllOrdersRequest(LTCUSDT);
        restClient.getAllOrders(orderRequest);
    }

    @Test
    public void newOCO() {
    }

    @Test
    public void cancelOrderList() {
    }

    @Test
    public void getOrderListStatus() {
    }

    @Test
    public void getAllOrderList() {
        final AllOrderListRequest request = new AllOrderListRequest();
        final List<OrderList> list = restClient.getAllOrderList(request);
        System.out.println();
    }

    @Test
    public void getAccount() {
        final Account account = restClient.getAccount();
        final List<AssetBalance> balances = account.getBalances();
        final int buyerCommission = account.getBuyerCommission();
        final AssetBalance btc = account.getAssetBalance("BTC");
        final int makerCommission = account.getMakerCommission();
        final int sellerCommission = account.getSellerCommission();
        final int takerCommission = account.getTakerCommission();
        final long updateTime = account.getUpdateTime();
        System.out.println();
    }

    @Test
    public void testGetAccount() {
    }

    @Test
    public void getMyTrades() {
    }

    @Test
    public void testGetMyTrades() {
    }

    @Test
    public void testGetMyTrades1() {
    }

    @Test
    public void testGetMyTrades2() {
    }

    @Test
    public void withdraw() {
    }

    @Test
    public void dustTranfer() {
    }

    @Test
    public void getDepositHistory() {
    }

    @Test
    public void getWithdrawHistory() {
    }

    @Test
    public void getSubAccountTransfers() {
    }

    @Test
    public void getDepositAddress() {
    }
}