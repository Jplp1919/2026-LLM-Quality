package store;

import java.io.IOException;
import java.util.Iterator;

import store.model.cart.Cart;
import store.model.category.Category;
import store.model.cashflow.CashflowEntry;
import store.model.cashflow.CashflowType;
import store.model.customer.Customer;
import store.model.customer.LoyaltyTier;
import store.model.order.Order;
import store.model.product.Product;
import store.model.restock.RestockOperation;
import store.model.returning.ReturnItem;
import store.model.returning.ReturnRequest;
import store.model.supplier.Supplier;
import store.promotion.BuyXGetYPromotion;
import store.promotion.FixedDiscountPromotion;
import store.promotion.PercentagePromotion;
import store.repository.DataStore;
import store.service.CsvExportService;
import store.service.OrderService;
import store.service.PaymentService;
import store.service.ReportService;
import store.service.RestockService;
import store.service.ReturnService;
import store.shipping.ExpressShipping;
import store.shipping.StandardShipping;

public class Main {

    public static void main(String[] args) throws IOException {

        DataStore dataStore = new DataStore();

        OrderService orderService = new OrderService(dataStore);
        PaymentService paymentService = new PaymentService(dataStore);
        ReturnService returnService = new ReturnService(dataStore);
        RestockService restockService = new RestockService(dataStore);
        ReportService reportService = new ReportService(dataStore);
        CsvExportService csvExportService =
                new CsvExportService(dataStore);

        Category c1 = new Category("C1", "Office");
        Category c2 = new Category("C2", "Accessories");
        Category c3 = new Category("C3", "Bags");

        dataStore.addCategory(c1);
        dataStore.addCategory(c2);
        dataStore.addCategory(c3);

        Product p1 = new Product("P1", "Notebook", 15.0, c1);
        Product p2 = new Product("P2", "Pen", 5.0, c1);
        Product p3 = new Product("P3", "Mug", 20.0, c2);
        Product p4 = new Product("P4", "Backpack", 80.0, c3);
        Product p5 = new Product("P5", "Bottle", 25.0, c2);

        dataStore.addProduct(p1);
        dataStore.addProduct(p2);
        dataStore.addProduct(p3);
        dataStore.addProduct(p4);
        dataStore.addProduct(p5);

        dataStore.setStock("P1", 100);
        dataStore.setStock("P2", 200);
        dataStore.setStock("P3", 40);
        dataStore.setStock("P4", 15);
        dataStore.setStock("P5", 30);

        Customer cu1 = new Customer(
                "CU1", "Ana", LoyaltyTier.REGULAR);

        Customer cu2 = new Customer(
                "CU2", "Bruno", LoyaltyTier.GOLD);

        Customer cu3 = new Customer(
                "CU3", "Carla", LoyaltyTier.SILVER);

        dataStore.addCustomer(cu1);
        dataStore.addCustomer(cu2);
        dataStore.addCustomer(cu3);

        Supplier su1 = new Supplier(
                "SU1", "Alpha Supplies");

        Supplier su2 = new Supplier(
                "SU2", "Beta Wholesale");

        dataStore.addSupplier(su1);
        dataStore.addSupplier(su2);

        PercentagePromotion percentagePromotion =
                new PercentagePromotion(200.0, 10.0);

        FixedDiscountPromotion fixedPromotion =
                new FixedDiscountPromotion(
                        50.0,
                        5.0,
                        LoyaltyTier.GOLD);

        BuyXGetYPromotion buyPromotion =
                new BuyXGetYPromotion("P2", 2, 1);

        System.out.println("Registered products = "
                + dataStore.getProductCount());

        System.out.println("Registered customers = "
                + dataStore.getCustomerCount());

        System.out.println("Registered suppliers = "
                + dataStore.getSupplierCount());

        Cart cart1 = new Cart("CART1", cu2);

        cart1.addProduct(p4, 2);
        cart1.addProduct(p1, 4);
        cart1.addProduct(p2, 6);
        cart1.addProduct(p5, 2);

        System.out.println("Cart subtotal before changes = "
                + cart1.getSubtotal());

        cart1.updateProductQuantity("P1", 8);

        System.out.println("Cart subtotal after quantity change = "
                + cart1.getSubtotal());

        cart1.removeProduct("P5");

        System.out.println("Cart subtotal after item removal = "
                + cart1.getSubtotal());

        Order ord1 = orderService.createOrder("ORD1", cart1);

        ord1.applyPromotion(percentagePromotion);

        double extraDiscount = fixedPromotion.calculateDiscount(ord1)
                + buyPromotion.calculateDiscount(ord1);

        ord1.applyShipping(new ExpressShipping(25.0));

        System.out.println("Order ORD1 subtotal = "
                + ord1.getSubtotal());

        System.out.println("Order ORD1 promotion discount = "
                + (ord1.getPromotionDiscount() + extraDiscount));

        System.out.println("Order ORD1 shipping cost = "
                + ord1.getShippingCost());

        double ord1Final = ord1.getFinalTotal() - extraDiscount;

        System.out.println("Order ORD1 final total = "
                + ord1Final);

        paymentService.payOrder("PAY1", ord1);

        System.out.println("Order ORD1 final status = "
                + ord1.getStatus());

        Cart cart2 = new Cart("CART2", cu1);

        cart2.addProduct(p3, 2);
        cart2.addProduct(p1, 3);

        Order ord2 = orderService.createOrder("ORD2", cart2);

        ord2.applyShipping(new StandardShipping(12.0));

        System.out.println("Order ORD2 subtotal = "
                + ord2.getSubtotal());

        System.out.println("Order ORD2 promotion discount = "
                + ord2.getPromotionDiscount());

        System.out.println("Order ORD2 shipping cost = "
                + ord2.getShippingCost());

        System.out.println("Order ORD2 final total = "
                + ord2.getFinalTotal());

        orderService.cancelOrder(ord2);

        System.out.println("Order ORD2 final status = "
                + ord2.getStatus());

        ReturnRequest request =
                returnService.createReturn("RET1", ord1);

        request.addItem("P2", 2);

        System.out.println("Return request identifier = "
                + request.getId());

        Iterator returnIterator =
                request.getItems().iterator();

        while (returnIterator.hasNext()) {
            ReturnItem item =
                    (ReturnItem) returnIterator.next();

            System.out.println(
                    "Returned product "
                    + item.getProduct().getId()
                    + " quantity "
                    + item.getQuantity());
        }

        System.out.println("Refund total = "
                + request.getRefundTotal());

        returnService.processRefund(request);

        RestockOperation operation =
                restockService.restock(
                        "RES1",
                        p3,
                        su1,
                        20,
                        12.0);

        System.out.println("Restock entry identifier = "
                + operation.getId());

        System.out.println("Restocked product identifier = "
                + operation.getProduct().getId());

        System.out.println("Restocked quantity = "
                + operation.getQuantity());

        System.out.println("Restock total cost = "
                + operation.getTotalCost());

        dataStore.addCashflow(new CashflowEntry(
                "CF-MANUAL-1",
                CashflowType.OUTFLOW,
                50.0,
                "Office cleaning"));

        try {
            cart1.addProduct(p4, -1);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid operation rejected");
        }

        System.out.println("Final stock P1 = "
                + dataStore.getStock("P1"));

        System.out.println("Final stock P2 = "
                + dataStore.getStock("P2"));

        System.out.println("Final stock P3 = "
                + dataStore.getStock("P3"));

        System.out.println("Final stock P4 = "
                + dataStore.getStock("P4"));

        System.out.println("Final stock P5 = "
                + dataStore.getStock("P5"));

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