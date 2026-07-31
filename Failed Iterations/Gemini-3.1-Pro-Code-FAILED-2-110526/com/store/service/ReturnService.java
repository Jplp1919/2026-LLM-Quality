package com.store.service;

import java.util.ArrayList;
import java.util.List;
import com.store.domain.CashflowType;
import com.store.domain.Order;
import com.store.domain.OrderItem;
import com.store.domain.OrderStatus;
import com.store.domain.ReturnRequest;
import com.store.domain.ReturnedItem;
import com.store.exception.StoreBusinessException;

public class ReturnService {
private InventoryService inventoryService;
private CashflowService cashflowService;
private List returnRequests = new ArrayList();

public ReturnService(InventoryService inventoryService, CashflowService cashflowService) {
    this.inventoryService = inventoryService;
    this.cashflowService = cashflowService;
}

public ReturnRequest createReturnRequest(String returnId, Order order, List<ReturnedItem> items) {
    if (order.getStatus() != OrderStatus.PAID) {
        throw new StoreBusinessException("Only paid orders can be returned");
    }
    double refundTotal = 0.0;
    for (ReturnedItem ri : items) {
        boolean found = false;
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getId().equals(ri.getProduct().getId())) {
                if (ri.getQuantity() > oi.getQuantity()) {
                    throw new StoreBusinessException("Returned quantity exceeds originally bought quantity");
                }
                refundTotal += ri.getQuantity() * oi.getUnitPrice();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new StoreBusinessException("Product not found in order");
        }
    }

    ReturnRequest request = new ReturnRequest(returnId, order, items, refundTotal);
    returnRequests.add(request);
    return request;
}

public void processRefund(ReturnRequest request) {
    for (ReturnedItem ri : request.getItems()) {
        inventoryService.increaseStock(ri.getProduct().getId(), ri.getQuantity());
    }
    cashflowService.recordEntry("REF" + request.getId(), request.getRefundTotal(), CashflowType.OUTFLOW, "Refund for return " + request.getId());
}

public List<ReturnRequest> getAllReturnRequests() {
    return new ArrayList<ReturnRequest>(returnRequests);
}
}