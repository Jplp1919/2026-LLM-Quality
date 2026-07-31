package com.store.main;

import com.store.catalog.CatalogService;
import com.store.catalog.Category;
import com.store.catalog.Product;
import com.store.customer.Customer;
import com.store.customer.CustomerService;
import com.store.customer.LoyaltyTier;
import com.store.exception.StoreBusinessException;
import com.store.export.CsvExportService;
import com.store.finance.CashflowEntry;
import com.store.finance.CashflowService;
import com.store.finance.CashflowType;
import com.store.finance.PaymentService;
import com.store.finance.ReturnRequest;
import com.store.finance.ReturnService;
import com.store.inventory.RestockOperation;
import com.store.inventory.RestockService;
import com.store.inventory.StockService;
import com.store.order.Cart;
import com.store.order.CartService;
import com.store.order.Order;
import com.store.order.OrderService;
import com.store.pricing.BuyXGetYPromotion;
import com.store.pricing.ExpressShipping;
import com.store.pricing.FixedDiscountPromotion;
import com.store.pricing.PercentagePromotion;
import com.store.pricing.StandardShipping;
import com.store.report.ReportService;
import com.store.supplier.Supplier;
import com.store.supplier.SupplierService;

public class Main {
    public static void main(String[] args) {
        CatalogService catalogService = new CatalogService();
        StockService stockService = new StockService();
        CustomerService customerService = new CustomerService();
        SupplierService supplierService = new SupplierService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService(catalogService, stockService, customerService);
        CashflowService cashflowService = new CashflowService();
        PaymentService paymentService = new PaymentService(cashflowService);
        ReturnService returnService = new ReturnService(orderService, stockService, cashflowService);
        RestockService restockService = new RestockService(stockService, supplierService, cashflowService);
        ReportService reportService = new ReportService(stockService, orderService, cashflowService, returnService, restockService);
        CsvExportService csvExportService = new CsvExportService();

        Category c1 = new Category("C1", "Office");
        Category c2 = new Category("C2", "Accessories");
        Category c3 = new Category("C3", "Bags");
        catalogService.registerCategory(c1);
        catalogService.registerCategory(c2);
        catalogService.registerCategory(c3);

        catalogService.registerProduct(new Product("P1", "Notebook", 15.0, c1));
        catalogService.registerProduct(new Product("P2", "Pen", 5.0, c1));
        catalogService.registerProduct(new Product("P3", "Mug", 20.0, c2));
        catalogService.registerProduct(new Product("P4", "Backpack", 80.0, c3));
        catalogService.registerProduct(new Product("P5", "Bottle", 25.0, c2));

        stockService.initializeStock("P1", 100);
        stockService.initializeStock("P2", 200);
        stockService.initializeStock("P3", 40);
        stockService.initializeStock("P4", 15);
        stockService.initializeStock("P5", 30);

        customerService.registerCustomer(new Customer("CU1", "Ana", LoyaltyTier.REGULAR));
        customerService.registerCustomer(new Customer("CU2", "Bruno", LoyaltyTier.GOLD));
        customerService.registerCustomer(new Customer("CU3", "Carla", LoyaltyTier.SILVER));

        supplierService.registerSupplier(new Supplier("SU1", "Alpha Supplies"));
        supplierService.registerSupplier(new Supplier("SU2", "Beta Wholesale"));

        PercentagePromotion pPromo = new PercentagePromotion(10.0, 200.0);
        FixedDiscountPromotion fPromo = new FixedDiscountPromotion(5.0, 50.0, LoyaltyTier.GOLD);
        BuyXGetYPromotion bPromo = new BuyXGetYPromotion("P2", 2, 1);

        StandardShipping standardShipping = new StandardShipping(12.0);
        ExpressShipping expressShipping = new ExpressShipping(25.0);

        System.out.println("number of registered products: " + catalogService.getProductCount());
        System.out.println("number of registered customers: " + customerService.getCustomerCount());
        System.out.println("number of registered suppliers: " + supplierService.getSupplierCount());

        Cart cart1 = cartService.createCart("CU2");
        cartService.addProduct(cart1, "P4", 1, catalogService);
        cartService.addProduct(cart1, "P1", 2, catalogService);
        cartService.addProduct(cart1, "P2", 4, catalogService);
        cartService.addProduct(cart1, "P5", 1, catalogService);
        System.out.println("cart subtotal before quantity/item changes: " + cartService.calculateSubtotalPreview(cart1, catalogService));

        cartService.updateQuantity(cart1, "P2", 5);
        System.out.println("cart subtotal after the quantity change: " + cartService.calculateSubtotalPreview(cart1, catalogService));

        cartService.removeProduct(cart1, "P5");
        System.out.println("cart subtotal after the item removal: " + cartService.calculateSubtotalPreview(cart1, catalogService));

        Order ord1 = orderService.createOrder("ORD1", cart1);
        orderService.applyPromotion(ord1, fPromo);
        orderService.applyShipping(ord1, expressShipping);

        System.out.println("order ORD1 subtotal: " + ord1.getSubtotal());
        System.out.println("order ORD1 promotion discount: " + ord1.getPromotionDiscount());
        System.out.println("order ORD1 shipping cost: " + ord1.getShippingCost());
        System.out.println("order ORD1 final total: " + ord1.getFinalTotal());

        paymentService.registerPayment(ord1);
        System.out.println("order ORD1 final status: " + ord1.getStatus());

        Cart cart2 = cartService.createCart("CU1");
        cartService.addProduct(cart2, "P2", 3, catalogService);
        cartService.addProduct(cart2, "P3", 1, catalogService);
        Order ord2 = orderService.createOrder("ORD2", cart2);
        orderService.applyPromotion(ord2, bPromo);
        orderService.applyShipping(ord2, standardShipping);

        System.out.println("order ORD2 subtotal: " + ord2.getSubtotal());
        System.out.println("order ORD2 promotion discount: " + ord2.getPromotionDiscount());
        System.out.println("order ORD2 shipping cost: " + ord2.getShippingCost());
        System.out.println("order ORD2 final total: " + ord2.getFinalTotal());

        orderService.cancelOrder("ORD2");
        System.out.println("order ORD2 final status: " + ord2.getStatus());

        ReturnRequest retReq = returnService.createReturnRequest("RET1", "ORD1");
        returnService.addReturnItem(retReq, "P1", 1);
        returnService.processRefund(retReq);

        System.out.println("return request identifier: " + retReq.getId());
        System.out.println("returned product identifiers and quantities: P1, 1");
        System.out.println("refund total: " + retReq.getRefundTotal());

        RestockOperation restockOp = restockService.registerRestock("RST1", "P4", "SU1", 10, 50.0);
        System.out.println("restock entry identifier: " + restockOp.getId());
        System.out.println("restocked product identifier and quantity: " + restockOp.getProductId() + ", " + restockOp.getQuantity());
        System.out.println("restock total cost: " + restockOp.getTotalCost());

        cashflowService.recordEntry(new CashflowEntry("MAN1", CashflowType.OUTFLOW, 100.0, "Manual Outflow"));

        System.out.println("final stock values of P1, P2, P3, P4 and P5:");
        System.out.println("P1: " + stockService.getStock("P1"));
        System.out.println("P2: " + stockService.getStock("P2"));
        System.out.println("P3: " + stockService.getStock("P3"));
        System.out.println("P4: " + stockService.getStock("P4"));
        System.out.println("P5: " + stockService.getStock("P5"));

        csvExportService.exportInventory(stockService, "inventory.csv");
        System.out.println("confirmation that inventory.csv was exported");
        csvExportService.exportCashflow(cashflowService, "cashflow.csv");
        System.out.println("confirmation that cashflow.csv was exported");

        try {
            stockService.decrease("P1", 5000);
        } catch (StoreBusinessException e) {
            System.out.println("confirmation that the invalid operation was rejected: " + e.getMessage());
        }

        reportService.printStockReport();
        reportService.printSalesByProductReport();
        reportService.printOrdersByStatusReport();
        reportService.printCashflowSummaryReport();
        reportService.printReturnsRefundsReport();
        reportService.printRestockingReport();
    }
}