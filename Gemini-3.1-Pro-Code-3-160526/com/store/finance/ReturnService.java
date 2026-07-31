package com.store.finance;

import com.store.exception.StoreBusinessException;
import com.store.inventory.StockService;
import com.store.order.Order;
import com.store.order.OrderItem;
import com.store.order.OrderService;
import com.store.order.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class ReturnService {
    private OrderService orderService;
    private StockService stockService;
    private CashflowService cashflowService;
    private List<ReturnRequest> returns = new ArrayList<ReturnRequest>();

    public ReturnService(OrderService orderService, StockService stockService, CashflowService cashflowService) {
        this.orderService = orderService;
        this.stockService = stockService;
        this.cashflowService = cashflowService;
    }

    public ReturnRequest createReturnRequest(String returnId, String orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            throw new StoreBusinessException("Order not found.");
        }
        if (order.getStatus() != OrderStatus.PAID) {
            throw new StoreBusinessException("Only paid orders can be returned.");
        }
        ReturnRequest request = new ReturnRequest(returnId, orderId);
        returns.add(request);
        return request;
    }

    public void addReturnItem(ReturnRequest request, String productId, int quantity) {
        if (quantity <= 0) {
            throw new StoreBusinessException("Returned quantity must be positive.");
        }
        Order order = orderService.getOrder(request.getOrderId());
        int originallyBought = 0;
        double unitPrice = 0.0;
        for (OrderItem item : order.getItems()) {
            if (item.getProductId().equals(productId)) {
                originallyBought += item.getQuantity();
                unitPrice = item.getUnitPrice();
            }
        }
        if (quantity > originallyBought) {
            throw new StoreBusinessException("The returned quantity cannot exceed the quantity originally bought.");
        }
        request.addItem(new ReturnItem(productId, quantity));
        request.setRefundTotal(request.getRefundTotal() + (quantity * unitPrice));
    }

    public void processRefund(ReturnRequest request) {
        if (request.isProcessed()) {
            throw new StoreBusinessException("Return request already processed.");
        }
        if (request.getItems().isEmpty()) {
            throw new StoreBusinessException("A return must contain at least one returned item.");
        }
        if (request.getRefundTotal() < 0) {
            throw new StoreBusinessException("Refund values cannot be negative.");
        }

        for (ReturnItem item : request.getItems()) {
            stockService.increase(item.getProductId(), item.getQuantity());
        }

        CashflowEntry entry = new CashflowEntry("REF_" + request.getId(), CashflowType.OUTFLOW, request.getRefundTotal(), "Refund for return " + request.getId());
        cashflowService.recordEntry(entry);
        
        Order order = orderService.getOrder(request.getOrderId());
        order.setStatus(OrderStatus.RETURNED);
        
        request.setProcessed(true);
    }

    public List<ReturnRequest> getAllReturns() {
        return new ArrayList<ReturnRequest>(returns);
    }
}