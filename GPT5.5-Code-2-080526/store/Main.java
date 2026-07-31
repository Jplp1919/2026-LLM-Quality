package store;

import java.io.IOException;

import store.model.cart.Cart;
import store.model.category.Category;
import store.model.customer.Customer;
import store.model.customer.LoyaltyTier;
import store.model.order.Order;
import store.model.product.Product;
import store.model.promotion.BuyXGetYPromotion;
import store.model.promotion.FixedDiscountPromotion;
import store.model.promotion.PercentagePromotion;
import store.model.restock.RestockOperation;
import store.model.returns.ReturnItem;
import store.model.returns.ReturnRequest;
import store.model.shipping.ExpressShipping;
import store.model.shipping.StandardShipping;
import store.model.supplier.Supplier;
import store.repository.DataStore;
import store.service.CashflowService;
import store.service.CatalogService;
import store.service.CsvExportService;
import store.service.CustomerService;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ReportService;
import store.service.RestockService;
import store.service.ReturnService;
import store.service.StockService;
import store.service.SupplierService;

public class Main {

    public static void main(String[] args) {

        DataStore dataStore = new DataStore();

        CatalogService catalogService = new CatalogService(dataStore);
        StockService stockService = new StockService(dataStore);
        CustomerService customerService = new CustomerService(dataStore);
        SupplierService supplierService = new SupplierService(dataStore);
        CashflowService cashflowService = new CashflowService(dataStore);
        OrderService orderService = new OrderService(dataStore, stockService);
        PaymentService paymentService = new PaymentService(cashflowService);
        ReturnService returnService = new ReturnService(
                dataStore,
                stockService,
                cashflowService);

        RestockService restockService = new RestockService(
                dataStore,
                stockService,
                cashflowService);

        ReportService reportService = new ReportService(
                dataStore,
                stockService,
                cashflowService);

        CsvExportService csvExportService = new CsvExportService(
                dataStore,
                stockService);

        Category c1 = new Category("C1", "Office");
        Category c2 = new Category("C2", "Accessories");
        Category c3 = new Category("C3", "Bags");

        catalogService.addCategory(c1);
        catalogService.addCategory(c2);
        catalogService.addCategory(c3);

        Product p1 = new Product("P1", "Notebook", 15.0, c1);
        Product p2 = new Product("P2", "Pen", 5.0, c1);
        Product p3 = new Product("P3", "Mug", 20.0, c2);
        Product p4 = new Product("P4", "Backpack", 80.0, c3);
        Product p5 = new Product("P5", "Bottle", 25.0, c2);

        catalogService.addProduct(p1);
        catalogService.addProduct(p2);
        catalogService.addProduct(p3);
        catalogService.addProduct(p4);
        catalogService.addProduct(p5);

        stockService.increaseStock("P1", 100);
        stockService.increaseStock("P2", 200);
        stockService.increaseStock("P3", 40);
        stockService.increaseStock("P4", 15);
        stockService.increaseStock("P5", 30);

        Customer cu1 = new Customer("CU1", "Ana", LoyaltyTier.REGULAR);
        Customer cu2 = new Customer("CU2", "Bruno", LoyaltyTier.GOLD);
        Customer cu3 = new Customer("CU3", "Carla", LoyaltyTier.SILVER);

        customerService.registerCustomer(cu1);
        customerService.registerCustomer(cu2);
        customerService.registerCustomer(cu3);

        Supplier su1 = new Supplier("SU1", "Alpha Supplies");
        Supplier su2 = new Supplier("SU2", "Beta Wholesale");

        supplierService.registerSupplier(su1);
        supplierService.registerSupplier(su2);

        PercentagePromotion percentagePromotion =
                new PercentagePromotion("10PERCENT", 200.0, 0.10);

        FixedDiscountPromotion fixedPromotion =
                new FixedDiscountPromotion(
                        "GOLD5",
                        50.0,
                        5.0,
                        LoyaltyTier.GOLD);

        BuyXGetYPromotion buyPromotion =
                new BuyXGetYPromotion("BUY2GET1", "P2", 2, 1);

        System.out.println("Registered products = " + dataStore.getProducts().size());
        System.out.println("Registered customers = " + dataStore.getCustomers().size());
        System.out.println("Registered suppliers = " + dataStore.getSuppliers().size());

        Cart cart1 = new Cart("CART1", cu2);

        cart1.addProduct(p4, 2);
        cart1.addProduct(p1, 4);
        cart1.addProduct(p2, 6);
        cart1.addProduct(p5, 2);

        System.out.println("Cart subtotal before changes = " + cart1.getSubtotal());

        cart1.updateQuantity("P1", 6);

        System.out.println("Cart subtotal after quantity change = " + cart1.getSubtotal());

        cart1.removeProduct("P5");

        System.out.println("Cart subtotal after item removal = " + cart1.getSubtotal());

        Order ord1 = orderService.createOrder("ORD1", cart1);

        orderService.applyPromotion(ord1, percentagePromotion);
        orderService.applyShipping(ord1, new ExpressShipping());

        System.out.println("ORD1 subtotal = " + ord1.getSubtotal());
        System.out.println("ORD1 promotion discount = " + ord1.getPromotionDiscount());
        System.out.println("ORD1 shipping cost = " + ord1.getShippingCost());
        System.out.println("ORD1 final total = " + ord1.getFinalTotal());

        paymentService.registerPayment("PAY1", ord1);

        System.out.println("ORD1 final status = " + ord1.getStatus());

        Cart cart2 = new Cart("CART2", cu1);

        cart2.addProduct(p3, 2);
        cart2.addProduct(p2, 3);

        Order ord2 = orderService.createOrder("ORD2", cart2);

        orderService.applyShipping(ord2, new StandardShipping());

        System.out.println("ORD2 subtotal = " + ord2.getSubtotal());
        System.out.println("ORD2 promotion discount = " + ord2.getPromotionDiscount());
        System.out.println("ORD2 shipping cost = " + ord2.getShippingCost());
        System.out.println("ORD2 final total = " + ord2.getFinalTotal());

        orderService.cancelOrder(ord2);

        System.out.println("ORD2 final status = " + ord2.getStatus());

        ReturnRequest returnRequest = new ReturnRequest("RET1", ord1);

        returnRequest.addItem("P1", 2);

        System.out.println("Return request identifier = " + returnRequest.getId());

        int i;

        for (i = 0; i < returnRequest.getItems().size(); i++) {
            ReturnItem item = (ReturnItem) returnRequest.getItems().get(i);

            System.out.println(
                    "Returned product "
                            + item.getOrderItem().getProduct().getId()
                            + " quantity "
                            + item.getQuantity());
        }

        System.out.println("Refund total = " + returnRequest.getRefundTotal());

        returnService.processRefund(returnRequest);

        RestockOperation restock =
                new RestockOperation("RES1", p4, su1, 10, 50.0);

        restockService.registerRestock(restock);

        System.out.println("Restock entry identifier = " + restock.getId());
        System.out.println(
                "Restocked product "
                        + restock.getProduct().getId()
                        + " quantity "
                        + restock.getQuantity());

        System.out.println("Restock total cost = " + restock.getTotalCost());

        cashflowService.addManualOutflow(
                "MAN1",
                40.0,
                "Operational expense");

        try {
            stockService.decreaseStock("P4", 1000);
        } catch (Exception e) {
            System.out.println("Invalid operation rejected");
        }

        System.out.println("Final stock P1 = " + stockService.getStock("P1"));
        System.out.println("Final stock P2 = " + stockService.getStock("P2"));
        System.out.println("Final stock P3 = " + stockService.getStock("P3"));
        System.out.println("Final stock P4 = " + stockService.getStock("P4"));
        System.out.println("Final stock P5 = " + stockService.getStock("P5"));

        reportService.printStockReport();
        reportService.printSalesByProductReport();
        reportService.printOrdersByStatusReport();
        reportService.printCashflowSummaryReport();
        reportService.printReturnsReport();
        reportService.printRestockingReport();

        try {
            csvExportService.exportInventory();
            csvExportService.exportCashflow();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Buy X Get Y promotion example discount = "
                + buyPromotion.calculateDiscount(ord1));

        System.out.println("Fixed promotion example discount = "
                + fixedPromotion.calculateDiscount(ord1));
    }
}