package store.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.order.Order;
import store.model.order.OrderItem;
import store.model.order.OrderStatus;
import store.model.restock.RestockOperation;
import store.model.returning.ReturnItem;
import store.model.returning.ReturnRequest;
import store.repository.DataStore;

public class ReportService {
    private DataStore dataStore;

    public ReportService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void printStockReport() {
        System.out.println("STOCK REPORT");

        Iterator iterator = dataStore.getProductsMap().values().iterator();

        while (iterator.hasNext()) {
            store.model.product.Product product =
                    (store.model.product.Product) iterator.next();

            System.out.println(product.getId() + " = "
                    + dataStore.getStock(product.getId()));
        }
    }

    public void printSalesByProductReport() {
        System.out.println("SALES BY PRODUCT REPORT");

        Map sales = new HashMap();

        Iterator orderIterator = dataStore.getOrders().iterator();

        while (orderIterator.hasNext()) {
            Order order = (Order) orderIterator.next();

            if (order.getStatus() == OrderStatus.PAID) {
                Iterator itemIterator = order.getItems().iterator();

                while (itemIterator.hasNext()) {
                    OrderItem item = (OrderItem) itemIterator.next();

                    Integer current = (Integer) sales.get(
                            item.getProduct().getId());

                    int quantity = item.getQuantity();

                    if (current != null) {
                        quantity += current.intValue();
                    }

                    sales.put(item.getProduct().getId(),
                            new Integer(quantity));
                }
            }
        }

        Iterator iterator = sales.keySet().iterator();

        while (iterator.hasNext()) {
            String productId = (String) iterator.next();

            System.out.println(productId + " sold = "
                    + sales.get(productId));
        }
    }

    public void printOrdersByStatusReport() {
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

        System.out.println("ORDERS BY STATUS REPORT");
        System.out.println("CREATED = " + created);
        System.out.println("PAID = " + paid);
        System.out.println("CANCELLED = " + cancelled);
    }

    public void printCashflowSummaryReport() {
        double inflow = 0;
        double outflow = 0;

        Iterator iterator = dataStore.getCashflows().iterator();

        while (iterator.hasNext()) {
            CashflowEntry entry = (CashflowEntry) iterator.next();

            if (entry.getType() == CashflowType.INFLOW) {
                inflow += entry.getAmount();
            } else {
                outflow += entry.getAmount();
            }
        }

        System.out.println("CASHFLOW SUMMARY REPORT");
        System.out.println("Total inflow = " + inflow);
        System.out.println("Total outflow = " + outflow);
        System.out.println("Net cashflow = " + (inflow - outflow));
    }

    public void printReturnsReport() {
        System.out.println("RETURNS REPORT");

        Iterator iterator = dataStore.getReturns().iterator();

        while (iterator.hasNext()) {
            ReturnRequest request = (ReturnRequest) iterator.next();

            System.out.println(request.getId()
                    + " refund total = "
                    + request.getRefundTotal());

            Iterator itemIterator = request.getItems().iterator();

            while (itemIterator.hasNext()) {
                ReturnItem item = (ReturnItem) itemIterator.next();

                System.out.println(item.getProduct().getId()
                        + " returned quantity = "
                        + item.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("RESTOCK REPORT");

        Iterator iterator = dataStore.getRestocks().iterator();

        while (iterator.hasNext()) {
            RestockOperation operation =
                    (RestockOperation) iterator.next();

            System.out.println(operation.getId()
                    + " product="
                    + operation.getProduct().getId()
                    + " supplier="
                    + operation.getSupplier().getId()
                    + " quantity="
                    + operation.getQuantity()
                    + " totalCost="
                    + operation.getTotalCost());
        }
    }
}