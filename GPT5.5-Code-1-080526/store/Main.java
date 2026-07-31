package store;

import store.model.cart.Cart;
import store.model.catalog.Category;
import store.model.catalog.Product;
import store.model.customer.Customer;
import store.model.customer.LoyaltyTier;
import store.model.order.Order;
import store.model.payment.Payment;
import store.model.promotion.BuyXGetYPromotion;
import store.model.promotion.FixedDiscountPromotion;
import store.model.promotion.PercentagePromotion;
import store.model.restock.RestockOperation;
import store.model.returns.ReturnRequest;
import store.model.shipping.ExpressShipping;
import store.model.shipping.PickupShipping;
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

    public static void main(String[] args) throws Exception {

        DataStore dataStore = new DataStore();

        CatalogService catalogService = new CatalogService(dataStore);
        StockService stockService = new StockService(dataStore);
        CustomerService customerService = new CustomerService(dataStore);
        SupplierService supplierService = new SupplierService(dataStore);
        OrderService orderService = new OrderService(dataStore, stockService);
        PaymentService paymentService = new PaymentService(dataStore);
        ReturnService returnService = new ReturnService(dataStore, stockService);
        RestockService restockService = new RestockService(dataStore, stockService);
        CashflowService cashflowService = new CashflowService(dataStore);
        ReportService reportService =
                new ReportService(dataStore, stockService, cashflowService);
        CsvExportService csvExportService =
                new CsvExportService(dataStore, stockService);

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

        stockService.increaseStock(p1, 100);
        stockService.increaseStock(p2, 200);
        stockService.increaseStock(p3, 40);
        stockService.increaseStock(p4, 15);
        stockService.increaseStock(p5, 30);

        Customer cu1 =
                new Customer("CU1", "Ana", LoyaltyTier.REGULAR);

        Customer cu2 =
                new Customer("CU2", "Bruno", LoyaltyTier.GOLD);

        Customer cu3 =
                new Customer("CU3", "Carla", LoyaltyTier.SILVER);

        customerService.registerCustomer(cu1);
        customerService.registerCustomer(cu2);
        customerService.registerCustomer(cu3);

        Supplier su1 = new Supplier("SU1", "Alpha Supplies");
        Supplier su2 = new Supplier("SU2", "Beta Wholesale");

        supplierService.registerSupplier(su1);
        supplierService.registerSupplier(su2);

        PercentagePromotion promo1 =
                new PercentagePromotion(
                        "10_PERCENT",
                        10.0,
                        200.0);

        FixedDiscountPromotion promo2 =
                new FixedDiscountPromotion(
                        "GOLD_5",
                        5.0,
                        50.0,
                        LoyaltyTier.GOLD);

        BuyXGetYPromotion promo3 =
                new BuyXGetYPromotion(
                        "BUY2GET1",
                        "P2",
                        2,
                        1);

        dataStore.getPromotions().add(promo1);
        dataStore.getPromotions().add(promo2);
        dataStore.getPromotions().add(promo3);

        System.out.println("REGISTERED PRODUCTS = " +
                dataStore.getProducts().size());

        System.out.println("REGISTERED CUSTOMERS = " +
                dataStore.getCustomers().size());

        System.out.println("REGISTERED SUPPLIERS = " +
                dataStore.getSuppliers().size());

        Cart cart1 = new Cart("CART1", cu2);

        cart1.addProduct(p4, 2);
        cart1.addProduct(p1, 4);
        cart1.addProduct(p2, 6);
        cart1.addProduct(p5, 2);

        System.out.println("CART SUBTOTAL BEFORE CHANGES = " +
                cart1.calculateSubtotal());

        cart1.updateProductQuantity("P1", 6);

        System.out.println("CART SUBTOTAL AFTER QUANTITY CHANGE = " +
                cart1.calculateSubtotal());

        cart1.removeProduct("P5");

        System.out.println("CART SUBTOTAL AFTER REMOVAL = " +
                cart1.calculateSubtotal());

        Order ord1 = orderService.createOrder("ORD1", cart1);

        orderService.applyPromotion(ord1, promo1);
        orderService.applyShipping(ord1, new ExpressShipping(25.0));

        System.out.println("ORDER ORD1 SUBTOTAL = " +
                ord1.calculateSubtotal());

        System.out.println("ORDER ORD1 PROMOTION DISCOUNT = " +
                ord1.getPromotionDiscount());

        System.out.println("ORDER ORD1 SHIPPING COST = " +
                ord1.getShippingCost());

        System.out.println("ORDER ORD1 FINAL TOTAL = " +
                ord1.calculateFinalTotal());

        paymentService.payOrder(
                ord1,
                new Payment("PAY1", ord1.calculateFinalTotal()));

        System.out.println("ORDER ORD1 FINAL STATUS = " +
                ord1.getStatus());

        Cart cart2 = new Cart("CART2", cu1);

        cart2.addProduct(p3, 2);
        cart2.addProduct(p5, 1);

        Order ord2 = orderService.createOrder("ORD2", cart2);

        orderService.applyShipping(ord2, new PickupShipping());

        System.out.println("ORDER ORD2 SUBTOTAL = " +
                ord2.calculateSubtotal());

        System.out.println("ORDER ORD2 PROMOTION DISCOUNT = " +
                ord2.getPromotionDiscount());

        System.out.println("ORDER ORD2 SHIPPING COST = " +
                ord2.getShippingCost());

        System.out.println("ORDER ORD2 FINAL TOTAL = " +
                ord2.calculateFinalTotal());

        orderService.cancelOrder(ord2);

        System.out.println("ORDER ORD2 FINAL STATUS = " +
                ord2.getStatus());

        ReturnRequest returnRequest =
                returnService.createReturn("RET1", ord1);

        returnService.addReturnedItem(returnRequest, "P1", 2);

        System.out.println("RETURN REQUEST IDENTIFIER = " +
                returnRequest.getId());

        System.out.println("RETURNED PRODUCT = P1 QUANTITY = 2");

        System.out.println("REFUND TOTAL = " +
                returnRequest.calculateRefundTotal());

        returnService.processRefund(returnRequest);

        RestockOperation restock =
                restockService.restock(
                        "RES1",
                        p2,
                        su1,
                        50,
                        2.0);

        System.out.println("RESTOCK ENTRY IDENTIFIER = " +
                restock.getId());

        System.out.println("RESTOCKED PRODUCT = " +
                restock.getProduct().getId());

        System.out.println("RESTOCKED QUANTITY = " +
                restock.getQuantity());

        System.out.println("RESTOCK TOTAL COST = " +
                restock.getTotalCost());

        cashflowService.registerManualOutflow(
                "OUT1",
                40.0,
                "Office maintenance");

        try {
            stockService.decreaseStock(p4, 1000);
            System.out.println("INVALID OPERATION ACCEPTED");
        } catch (Exception e) {
            System.out.println("INVALID OPERATION REJECTED");
        }

        System.out.println("FINAL STOCK P1 = " +
                stockService.getStock("P1"));

        System.out.println("FINAL STOCK P2 = " +
                stockService.getStock("P2"));

        System.out.println("FINAL STOCK P3 = " +
                stockService.getStock("P3"));

        System.out.println("FINAL STOCK P4 = " +
                stockService.getStock("P4"));

        System.out.println("FINAL STOCK P5 = " +
                stockService.getStock("P5"));

        csvExportService.exportInventory();
        csvExportService.exportCashflow();

        reportService.printStockReport();
        reportService.printSalesByProductReport();
        reportService.printOrdersByStatusReport();
        reportService.printCashflowSummaryReport();
        reportService.printReturnsReport();
        reportService.printRestockingReport();
    }
}