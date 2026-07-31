package store.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import store.model.cashflow.CashflowEntry;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;
import store.model.restock.RestockOperation;
import store.model.returns.ReturnItem;
import store.model.returns.ReturnRequest;
import store.repository.DataStore;

public class ReportService {

    private DataStore dataStore;
    private StockService stockService;
    private CashflowService cashflowService;

    public ReportService(DataStore dataStore,
            StockService stockService,
            CashflowService cashflowService) {

        this.dataStore = dataStore;
        this.stockService = stockService;
        this.cashflowService = cashflowService;
    }

    public void printStockReport() {
        System.out.println("STOCK REPORT");

        Iterator<String> iterator = dataStore.getProducts().keySet().iterator();

        while (iterator.hasNext()) {
            String id = (String) iterator.next();

            System.out.println(id + " = " + stockService.getStock(id));
        }
    }

    public void printSalesByProductReport() {
        System.out.println("SALES BY PRODUCT REPORT");

        Map<String, Integer> totals = new HashMap<String, Integer>();

        int i;
        int j;

        for (i = 0; i < dataStore.getOrders().size(); i++) {
            Order order = (Order) dataStore.getOrders().get(i);

            if (order.getStatus() == OrderStatus.PAID) {
                for (j = 0; j < order.getItems().size(); j++) {
                    OrderItem item = (OrderItem) order.getItems().get(j);

                    Integer value = (Integer) totals.get(item.getProduct().getId());

                    if (value == null) {
                        value = new Integer(0);
                    }

                    totals.put(
                            item.getProduct().getId(),
                            new Integer(value.intValue() + item.getQuantity()));
                }
            }
        }

        Iterator<String> iterator = totals.keySet().iterator();

        while (iterator.hasNext()) {
            String id = (String) iterator.next();

            System.out.println(id + " sold = " + totals.get(id));
        }
    }

    public void printOrdersByStatusReport() {
        System.out.println("ORDERS BY STATUS REPORT");

        int created = 0;
        int paid = 0;
        int cancelled = 0;

        int i;

        for (i = 0; i < dataStore.getOrders().size(); i++) {
            Order order = (Order) dataStore.getOrders().get(i);

            if (order.getStatus() == OrderStatus.CREATED) {
                created++;
            } else if (order.getStatus() == OrderStatus.PAID) {
                paid++;
            } else if (order.getStatus() == OrderStatus.CANCELLED) {
                cancelled++;
            }
        }

        System.out.println("CREATED = " + created);
        System.out.println("PAID = " + paid);
        System.out.println("CANCELLED = " + cancelled);
    }

    public void printCashflowSummaryReport() {
        System.out.println("CASHFLOW SUMMARY REPORT");

        System.out.println("INFLOW = " + cashflowService.getTotalInflow());
        System.out.println("OUTFLOW = " + cashflowService.getTotalOutflow());
        System.out.println("NET = " + cashflowService.getNetCashflow());

        int i;

        for (i = 0; i < dataStore.getCashflows().size(); i++) {
            CashflowEntry entry = (CashflowEntry) dataStore.getCashflows().get(i);

            System.out.println(entry.getId() + " " + entry.getType() + " " + entry.getAmount());
        }
    }

    public void printReturnsReport() {
        System.out.println("RETURNS REPORT");

        int i;
        int j;

        for (i = 0; i < dataStore.getReturns().size(); i++) {
            ReturnRequest request = (ReturnRequest) dataStore.getReturns().get(i);

            System.out.println(request.getId() + " refund = " + request.getRefundTotal());

            for (j = 0; j < request.getItems().size(); j++) {
                ReturnItem item = (ReturnItem) request.getItems().get(j);

                System.out.println(
                        item.getOrderItem().getProduct().getId()
                                + " returned = "
                                + item.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("RESTOCK REPORT");

        int i;

        for (i = 0; i < dataStore.getRestocks().size(); i++) {
            RestockOperation operation = (RestockOperation) dataStore.getRestocks().get(i);

            System.out.println(
                    operation.getId()
                            + " "
                            + operation.getProduct().getId()
                            + " "
                            + operation.getSupplier().getId()
                            + " "
                            + operation.getQuantity()
                            + " "
                            + operation.getTotalCost());
        }
    }
}