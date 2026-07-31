package com.store.main;

import java.util.ArrayList;
import java.util.List;

import com.store.domain.Cart;
import com.store.domain.Category;
import com.store.domain.Customer;
import com.store.domain.LoyaltyTier;
import com.store.domain.Order;
import com.store.domain.Product;
import com.store.domain.ReturnedItem;
import com.store.domain.Supplier;
import com.store.domain.CashflowType;
import com.store.domain.promotion.BuyXGetYPromotion;
import com.store.domain.promotion.FixedDiscountPromotion;
import com.store.domain.promotion.PercentagePromotion;
import com.store.domain.promotion.Promotion;
import com.store.domain.shipping.ExpressShipping;
import com.store.domain.shipping.ShippingRule;
import com.store.domain.shipping.StandardShipping;
import com.store.exception.StoreBusinessException;
import com.store.service.CartService;
import com.store.service.CashflowService;
import com.store.service.CatalogService;
import com.store.service.CustomerService;
import com.store.service.InventoryService;
import com.store.service.OrderService;
import com.store.service.PaymentService;
import com.store.service.ReportService;
import com.store.service.RestockService;
import com.store.service.ReturnService;
import com.store.service.SupplierService;

public class Main {
public static void main(String[] args) {
CatalogService catalogService = new CatalogService();
InventoryService inventoryService = new InventoryService();
CustomerService customerService = new CustomerService();
SupplierService supplierService = new SupplierService();
CartService cartService = new CartService();
OrderService orderService = new OrderService(inventoryService);
CashflowService cashflowService = new CashflowService();
PaymentService paymentService = new PaymentService(cashflowService);
ReturnService returnService = new ReturnService(inventoryService, cashflowService);
RestockService restockService = new RestockService(inventoryService, cashflowService);
ReportService reportService = new ReportService(inventoryService, orderService, returnService, restockService, cashflowService);

    Category c1 = new Category("C1", "Office");
    Category c2 = new Category("C2", "Accessories");
    Category c3 = new Category("C3", "Bags");
    catalogService.registerCategory(c1);
    catalogService.registerCategory(c2);
    catalogService.registerCategory(c3);

    Product p1 = new Product("P1", "Notebook", 15.0, c1);
    Product p2 = new Product("P2", "Pen", 5.0, c1);
    Product p3 = new Product("P3", "Mug", 20.0, c2);
    Product p4 = new Product("P4", "Backpack", 80.0, c3);
    Product p5 = new Product("P5", "Bottle", 25.0, c2);
    catalogService.registerProduct(p1);
    catalogService.registerProduct(p2);
    catalogService.registerProduct(p3);
    catalogService.registerProduct(p4);
    catalogService.registerProduct(p5);

    inventoryService.increaseStock("P1", 100);
    inventoryService.increaseStock("P2", 200);
    inventoryService.increaseStock("P3", 40);
    inventoryService.increaseStock("P4", 15);
    inventoryService.increaseStock("P5", 30);

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

    System.out.println("Number of registered products: " + catalogService.getProductCount());
    System.out.println("Number of registered customers: " + customerService.getCustomerCount());
    System.out.println("Number of registered suppliers: " + supplierService.getSupplierCount());

    Promotion promo1 = new PercentagePromotion(10.0, 200.0);
    Promotion promo2 = new FixedDiscountPromotion(5.0, 50.0, LoyaltyTier.GOLD);
    Promotion promo3 = new BuyXGetYPromotion(p2, 2, 1);

    ShippingRule expressShipping = new ExpressShipping(25.0);
    ShippingRule standardShipping = new StandardShipping(12.0);

    Cart cart1 = cartService.createCart(cu2);
    cartService.addProductToCart(cart1, p4, 1);
    cartService.addProductToCart(cart1, p1, 2);
    cartService.addProductToCart(cart1, p2, 3);
    cartService.addProductToCart(cart1, p5, 1);
    System.out.println("Cart subtotal before quantity/item changes: " + cart1.getSubtotalPreview());

    cartService.updateCartItemQuantity(cart1, "P1", 4);
    System.out.println("Cart subtotal after the quantity change: " + cart1.getSubtotalPreview());

    cartService.removeProductFromCart(cart1, "P5");
    System.out.println("Cart subtotal after the item removal: " + cart1.getSubtotalPreview());

    Order ord1 = orderService.createOrderFromCart("ORD1", cart1);
    orderService.applyPromotion(ord1, promo2);
    orderService.applyShipping(ord1, expressShipping);

    System.out.println("Order ORD1 subtotal: " + ord1.getSubtotal());
    System.out.println("Order ORD1 promotion discount: " + ord1.getPromotionDiscount());
    System.out.println("Order ORD1 shipping cost: " + ord1.getShippingCost());
    System.out.println("Order ORD1 final total: " + ord1.getFinalTotal());
    
    paymentService.registerPayment(ord1);
    System.out.println("Order ORD1 final status: " + ord1.getStatus());

    Cart cart2 = cartService.createCart(cu1);
    cartService.addProductToCart(cart2, p3, 2);
    cartService.addProductToCart(cart2, p1, 1);
    
    Order ord2 = orderService.createOrderFromCart("ORD2", cart2);
    orderService.applyShipping(ord2, standardShipping);
    System.out.println("Order ORD2 subtotal: " + ord2.getSubtotal());
    System.out.println("Order ORD2 promotion discount: " + ord2.getPromotionDiscount());
    System.out.println("Order ORD2 shipping cost: " + ord2.getShippingCost());
    System.out.println("Order ORD2 final total: " + ord2.getFinalTotal());

    orderService.cancelOrder(ord2);
    System.out.println("Order ORD2 final status: " + ord2.getStatus());

    List<ReturnedItem> retItems = new ArrayList<ReturnedItem>();
    retItems.add(new ReturnedItem(p4, 1));
    retItems.add(new ReturnedItem(p1, 2));
    var returnReq = returnService.createReturnRequest("RET1", ord1, retItems);
    
    System.out.println("Return request identifier: " + returnReq.getId());
    for (ReturnedItem ri : returnReq.getItems()) {
        System.out.println("Returned product identifier: " + ri.getProduct().getId() + " | quantity: " + ri.getQuantity());
    }
    System.out.println("Refund total: " + returnReq.getRefundTotal());
    returnService.processRefund(returnReq);

    var restockOp = restockService.registerRestock("R1", p3, su1, 10, 15.0);
    System.out.println("Restock entry identifier: " + restockOp.getId());
    System.out.println("Restocked product identifier: " + restockOp.getProduct().getId() + " | quantity: " + restockOp.getQuantity());
    System.out.println("Restock total cost: " + restockOp.getTotalCost());

    cashflowService.recordEntry("MAN1", 50.0, CashflowType.OUTFLOW, "Manual operational outflow");

    System.out.println("Final stock values:");
    System.out.println("P1: " + inventoryService.getStock("P1"));
    System.out.println("P2: " + inventoryService.getStock("P2"));
    System.out.println("P3: " + inventoryService.getStock("P3"));
    System.out.println("P4: " + inventoryService.getStock("P4"));
    System.out.println("P5: " + inventoryService.getStock("P5"));

    inventoryService.exportToCsv("inventory.csv");
    System.out.println("inventory.csv was exported");
    cashflowService.exportToCsv("cashflow.csv");
    System.out.println("cashflow.csv was exported");

    try {
        catalogService.deactivateProduct("P5");
        cartService.addProductToCart(cart1, p5, 1);
    } catch (StoreBusinessException e) {
        System.out.println("Invalid operation rejected: " + e.getMessage());
    }

    reportService.printStockReport();
    reportService.printSalesByProductReport();
    reportService.printOrdersByStatusReport();
    reportService.printCashflowSummaryReport();
    reportService.printReturnsRefundsReport();
    reportService.printRestockingReport();
}
}