package store.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import store.model.cashflow.CashflowEntry;
import store.model.catalog.Product;
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

    public ReportService(
            DataStore dataStore,
            StockService stockService,
            CashflowService cashflowService) {

        this.dataStore = dataStore;
        this.stockService = stockService;
        this.cashflowService = cashflowService;
    }

    public void printStockReport() {
        System.out.println("STOCK REPORT");

        Iterator iterator = dataStore.getProducts().values().iterator();

        while (iterator.hasNext()) {
            Product product = (Product) iterator.next();

            System.out.println(product.getId() + " = " +
                    stockService.getStock(product.getId()));
        }
    }

    public void printSalesByProductReport() {
        System.out.println("SALES BY PRODUCT REPORT");

        Map sold = new HashMap();

        Iterator iterator = dataStore.getOrders().iterator();

        while (iterator.hasNext()) {
            Order order = (Order) iterator.next();

            if (order.getStatus() == OrderStatus.PAID) {
                Iterator itemIterator = order.getItems().iterator();

                while (itemIterator.hasNext()) {
                    OrderItem item = (OrderItem) itemIterator.next();

                    Integer current =
                            (Integer) sold.get(item.getProduct().getId());

                    if (current == null) {
                        current = new Integer(0);
                    }

                    sold.put(item.getProduct().getId(),
                            new Integer(current.intValue() + item.getQuantity()));
                }
            }
        }

        Iterator soldIterator = sold.keySet().iterator();

        while (soldIterator.hasNext()) {
            String key = (String) soldIterator.next();

            System.out.println(key + " = " + sold.get(key));
        }
    }

    public void printOrdersByStatusReport() {
        System.out.println("ORDERS BY STATUS REPORT");

        int created = 0;
        int paid = 0;
        int cancelled = 0;

        Iterator iterator = dataStore.getOrders().iterator();

        while (iterator.hasNext()) {
            Order order = (Order) iterator.next();

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
        System.out.println("INFLOW = " + cashflowService.totalInflow());
        System.out.println("OUTFLOW = " + cashflowService.totalOutflow());
        System.out.println("NET = " + cashflowService.netCashflow());
    }

    public void printReturnsReport() {
        System.out.println("RETURNS REPORT");

        Iterator iterator = dataStore.getReturns().iterator();

        while (iterator.hasNext()) {
            ReturnRequest request = (ReturnRequest) iterator.next();

            System.out.println(request.getId() +
                    " REFUND = " + request.calculateRefundTotal());

            Iterator itemIterator = request.getItems().iterator();

            while (itemIterator.hasNext()) {
                ReturnItem item = (ReturnItem) itemIterator.next();

                System.out.println(item.getProduct().getId() +
                        " RETURNED = " + item.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("RESTOCKING REPORT");

        Iterator iterator = dataStore.getRestocks().iterator();

        while (iterator.hasNext()) {
            RestockOperation operation = (RestockOperation) iterator.next();

            System.out.println(
                    operation.getId() +
                    " PRODUCT = " + operation.getProduct().getId() +
                    " SUPPLIER = " + operation.getSupplier().getId() +
                    " QUANTITY = " + operation.getQuantity() +
                    " TOTAL = " + operation.getTotalCost());
        }
    }
}