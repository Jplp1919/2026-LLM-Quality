package com.store.service;

import java.util.HashMap;
import java.util.Map;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.RestockEntry;
import com.store.domain.ReturnItem;
import com.store.domain.ReturnRequest;
import com.store.domain.enums.OrderStatus;

public class ReportingService {
    private InventoryService inventoryService;
    private OrderService orderService;
    private CashflowService cashflowService;

    public ReportingService(InventoryService inventoryService, OrderService orderService, CashflowService cashflowService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.cashflowService = cashflowService;
    }

    public void printStockReport() {
        System.out.println("--- Stock Report ---");
        Map<String, Integer> stock = inventoryService.getCompleteStock();
        for (String id : stock.keySet()) {
            System.out.println("Product: " + id + " | Quantity: " + stock.get(id));
        }
    }

    public void printSalesReport() {
        System.out.println("--- Sales by Product Report ---");
        Map<String, Integer> sales = new HashMap<String, Integer>();
        for (Order o : orderService.getOrders()) {
            if (o.getStatus() == OrderStatus.PAID || o.getStatus() == OrderStatus.RETURNED) {
                for (OrderItem oi : o.getItems()) {
                    String pid = oi.getProduct().getId();
                    Integer current = sales.get(pid);
                    if (current == null) current = 0;
                    sales.put(pid, current + oi.getQuantity());
                }
            }
        }
        for (String pid : sales.keySet()) {
            System.out.println("Product: " + pid + " | Sold Quantity: " + sales.get(pid));
        }
    }

    public void printOrdersByStatusReport() {
        System.out.println("--- Orders by Status Report ---");
        Map<OrderStatus, Integer> counts = new HashMap<OrderStatus, Integer>();
        for (OrderStatus st : OrderStatus.values()) {
            counts.put(st, 0);
        }
        for (Order o : orderService.getOrders()) {
            counts.put(o.getStatus(), counts.get(o.getStatus()) + 1);
        }
        for (OrderStatus st : OrderStatus.values()) {
            System.out.println("Status: " + st.name() + " | Count: " + counts.get(st));
        }
    }

    public void printCashflowSummaryReport() {
        System.out.println("--- Cashflow Summary Report ---");
        System.out.println("Total Inflow: " + cashflowService.getTotalInflow());
        System.out.println("Total Outflow: " + cashflowService.getTotalOutflow());
        System.out.println("Net Cashflow: " + cashflowService.getNetCashflow());
    }

    public void printReturnsReport() {
        System.out.println("--- Returns/Refunds Report ---");
        for (ReturnRequest req : orderService.getReturns()) {
            System.out.println("Return ID: " + req.getId() + " | Refund Total: " + req.getRefundTotal());
            for (ReturnItem ri : req.getItems()) {
                System.out.println("  Returned Product: " + ri.getProduct().getId() + " | Quantity: " + ri.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("--- Restocking Report ---");
        for (RestockEntry e : inventoryService.getRestocks()) {
            System.out.println("Restock ID: " + e.getId() + " | Product: " + e.getProduct().getId() + " | Supplier: " + e.getSupplier().getId() + " | Quantity: " + e.getQuantity() + " | Total Cost: " + e.getTotalCost());
        }
    }
}