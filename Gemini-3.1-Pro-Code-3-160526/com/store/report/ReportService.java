package com.store.report;

import com.store.finance.CashflowService;
import com.store.finance.ReturnItem;
import com.store.finance.ReturnRequest;
import com.store.finance.ReturnService;
import com.store.inventory.RestockOperation;
import com.store.inventory.RestockService;
import com.store.inventory.StockService;
import com.store.order.Order;
import com.store.order.OrderItem;
import com.store.order.OrderService;
import com.store.order.OrderStatus;

import java.util.HashMap;
import java.util.Map;

public class ReportService {
    private StockService stockService;
    private OrderService orderService;
    private CashflowService cashflowService;
    private ReturnService returnService;
    private RestockService restockService;

    public ReportService(StockService stockService, OrderService orderService, CashflowService cashflowService, ReturnService returnService, RestockService restockService) {
        this.stockService = stockService;
        this.orderService = orderService;
        this.cashflowService = cashflowService;
        this.returnService = returnService;
        this.restockService = restockService;
    }

    public void printStockReport() {
        System.out.println("--- Stock Report ---");
        for (Map.Entry<String, Integer> entry : stockService.getAllStock().entrySet()) {
            System.out.println("Product: " + entry.getKey() + " | Stock: " + entry.getValue());
        }
    }

    public void printSalesByProductReport() {
        System.out.println("--- Sales by Product Report ---");
        Map<String, Integer> sales = new HashMap<String, Integer>();
        for (Order order : orderService.getAllOrders()) {
            if (order.getStatus() != OrderStatus.CANCELLED) {
                for (OrderItem item : order.getItems()) {
                    int current = sales.containsKey(item.getProductId()) ? sales.get(item.getProductId()) : 0;
                    sales.put(item.getProductId(), current + item.getQuantity());
                }
            }
        }
        for (Map.Entry<String, Integer> entry : sales.entrySet()) {
            System.out.println("Product: " + entry.getKey() + " | Sold Quantity: " + entry.getValue());
        }
    }

    public void printOrdersByStatusReport() {
        System.out.println("--- Orders by Status Report ---");
        Map<OrderStatus, Integer> counts = new HashMap<OrderStatus, Integer>();
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status, 0);
        }
        for (Order order : orderService.getAllOrders()) {
            counts.put(order.getStatus(), counts.get(order.getStatus()) + 1);
        }
        for (Map.Entry<OrderStatus, Integer> entry : counts.entrySet()) {
            System.out.println("Status: " + entry.getKey() + " | Count: " + entry.getValue());
        }
    }

    public void printCashflowSummaryReport() {
        System.out.println("--- Cashflow Summary Report ---");
        System.out.println("Total Inflow: " + cashflowService.getTotalInflow());
        System.out.println("Total Outflow: " + cashflowService.getTotalOutflow());
        System.out.println("Net Cashflow: " + cashflowService.getNetCashflow());
    }

    public void printReturnsRefundsReport() {
        System.out.println("--- Returns/Refunds Report ---");
        for (ReturnRequest req : returnService.getAllReturns()) {
            System.out.println("Return ID: " + req.getId() + " | Refund Total: " + req.getRefundTotal());
            for (ReturnItem item : req.getItems()) {
                System.out.println("  Returned Product: " + item.getProductId() + " | Quantity: " + item.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("--- Restocking Report ---");
        for (RestockOperation op : restockService.getAllOperations()) {
            System.out.println("Restock ID: " + op.getId() + " | Product: " + op.getProductId() + " | Supplier: " + op.getSupplierId() + " | Quantity: " + op.getQuantity() + " | Total Cost: " + op.getTotalCost());
        }
    }
}