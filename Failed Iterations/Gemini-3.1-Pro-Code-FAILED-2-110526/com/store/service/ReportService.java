package com.store.service;

import java.util.HashMap;
import java.util.Map;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.OrderStatus;
import com.store.domain.RestockOperation;
import com.store.domain.ReturnRequest;

public class ReportService {
private InventoryService inventoryService;
private OrderService orderService;
private ReturnService returnService;
private RestockService restockService;
private CashflowService cashflowService;

public ReportService(InventoryService inventoryService, OrderService orderService, ReturnService returnService, RestockService restockService, CashflowService cashflowService) {
    this.inventoryService = inventoryService;
    this.orderService = orderService;
    this.returnService = returnService;
    this.restockService = restockService;
    this.cashflowService = cashflowService;
}

public void printStockReport() {
    System.out.println("--- STOCK REPORT ---");
    for (Map.Entry<String, Integer> entry : inventoryService.getAllStock().entrySet()) {
        System.out.println("Product: " + entry.getKey() + " | Stock: " + entry.getValue());
    }
}

public void printSalesByProductReport() {
    System.out.println("--- SALES BY PRODUCT REPORT ---");
    Map<String, Integer> sales = new HashMap<String, Integer>();
    for (Order o : orderService.getAllOrders()) {
        if (o.getStatus() == OrderStatus.PAID) {
            for (OrderItem oi : o.getItems()) {
                Integer qty = sales.get(oi.getProduct().getId());
                if (qty == null) qty = 0;
                sales.put(oi.getProduct().getId(), qty + oi.getQuantity());
            }
        }
    }
    for (Map.Entry<String, Integer> entry : sales.entrySet()) {
        System.out.println("Product: " + entry.getKey() + " | Sold: " + entry.getValue());
    }
}

public void printOrdersByStatusReport() {
    System.out.println("--- ORDERS BY STATUS REPORT ---");
    Map<OrderStatus, Integer> counts = new HashMap<OrderStatus, Integer>();
    for (Order o : orderService.getAllOrders()) {
        Integer count = counts.get(o.getStatus());
        if (count == null) count = 0;
        counts.put(o.getStatus(), count + 1);
    }
    for (OrderStatus status : OrderStatus.values()) {
        Integer count = counts.get(status);
        System.out.println("Status: " + status + " | Count: " + (count == null ? 0 : count));
    }
}

public void printCashflowSummaryReport() {
    System.out.println("--- CASHFLOW SUMMARY REPORT ---");
    System.out.println("Total Inflow: " + cashflowService.getTotalInflow());
    System.out.println("Total Outflow: " + cashflowService.getTotalOutflow());
    System.out.println("Net Cashflow: " + cashflowService.getNetCashflow());
}

public void printReturnsRefundsReport() {
    System.out.println("--- RETURNS AND REFUNDS REPORT ---");
    for (ReturnRequest req : returnService.getAllReturnRequests()) {
        System.out.println("Return ID: " + req.getId() + " | Refund Total: " + req.getRefundTotal());
    }
}

public void printRestockingReport() {
    System.out.println("--- RESTOCKING REPORT ---");
    for (RestockOperation op : restockService.getAllRestockOperations()) {
        System.out.println("Restock ID: " + op.getId() + " | Product: " + op.getProduct().getId() + " | Supplier: " + op.getSupplier().getId() + " | Quantity: " + op.getQuantity() + " | Cost: " + op.getTotalCost());
    }
}
}