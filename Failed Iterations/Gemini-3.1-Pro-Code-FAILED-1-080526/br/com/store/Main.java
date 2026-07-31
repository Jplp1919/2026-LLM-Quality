package br.com.store;

import br.com.store.domain.Cart;
import br.com.store.domain.Category;
import br.com.store.domain.Customer;
import br.com.store.domain.LoyaltyTier;
import br.com.store.domain.Order;
import br.com.store.domain.OrderItem;
import br.com.store.domain.Product;
import br.com.store.domain.Restock;
import br.com.store.domain.ReturnItem;
import br.com.store.domain.ReturnRequest;
import br.com.store.domain.ShippingRule;
import br.com.store.domain.Supplier;
import br.com.store.domain.OrderStatus;
import br.com.store.promotion.BuyXGetYPromotion;
import br.com.store.promotion.FixedTierPromotion;
import br.com.store.promotion.PercentagePromotion;
import br.com.store.service.CatalogService;
import br.com.store.service.FinanceService;
import br.com.store.service.InventoryService;
import br.com.store.service.OrderService;
import br.com.store.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class Main {

public static void main(String[] args) {
    CatalogService catalogService = new CatalogService();
    FinanceService financeService = new FinanceService();
    InventoryService inventoryService = new InventoryService(financeService);
    UserService userService = new UserService();
    OrderService orderService = new OrderService(inventoryService, financeService);

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

    inventoryService.initializeStock(p1, 100);
    inventoryService.initializeStock(p2, 200);
    inventoryService.initializeStock(p3, 40);
    inventoryService.initializeStock(p4, 15);
    inventoryService.initializeStock(p5, 30);

    Customer cu1 = new Customer("CU1", "Ana", LoyaltyTier.REGULAR);
    Customer cu2 = new Customer("CU2", "Bruno", LoyaltyTier.GOLD);
    Customer cu3 = new Customer("CU3", "Carla", LoyaltyTier.SILVER);
    userService.registerCustomer(cu1);
    userService.registerCustomer(cu2);
    userService.registerCustomer(cu3);

    Supplier su1 = new Supplier("SU1", "Alpha Supplies");
    Supplier su2 = new Supplier("SU2", "Beta Wholesale");
    userService.registerSupplier(su1);
    userService.registerSupplier(su2);

    PercentagePromotion percentPromo = new PercentagePromotion(0.10, 200.0);
    FixedTierPromotion fixedPromo = new FixedTierPromotion(5.0, LoyaltyTier.GOLD, 50.0);
    BuyXGetYPromotion buyXGetYPromo = new BuyXGetYPromotion(p2, 2, 1);

    ShippingRule standardShipping = new ShippingRule("STANDARD", 12.0);
    ShippingRule expressShipping = new ShippingRule("EXPRESS", 25.0);
    ShippingRule pickupShipping = new ShippingRule("PICKUP", 0.0);

    Cart cart1 = new Cart(cu2);
    cart1.addItem(p4, 1);
    cart1.addItem(p1, 2);
    cart1.addItem(p2, 4);
    cart1.addItem(p5, 1);

    double subtotalBeforeChange = cart1.getSubtotal();
    cart1.updateItemQuantity(p1, 3);
    double subtotalAfterQuantityChange = cart1.getSubtotal();
    cart1.removeItem(p5);
    double subtotalAfterRemoval = cart1.getSubtotal();

    Order ord1 = orderService.createOrder("ORD1", cart1);
    orderService.applyPromotion(ord1, fixedPromo);
    orderService.applyShipping(ord1, expressShipping);
    orderService.payOrder(ord1);

    Cart cart2 = new Cart(cu1);
    cart2.addItem(p3, 2);
    Order ord2 = orderService.createOrder("ORD2", cart2);
    orderService.applyShipping(ord2, standardShipping);
    orderService.cancelOrder(ord2);

    ReturnRequest returnRequest = orderService.createReturnRequest(ord1);
    returnRequest.addReturnItem(p1, 1);
    orderService.processReturn(returnRequest);

    Restock restock = inventoryService.performRestock(p4, su1, 10, 50.0);
    financeService.registerOutflow(1000.0, "Rent");

    inventoryService.exportInventoryCsv("inventory.csv");
    financeService.exportCashflowCsv("cashflow.csv");

    boolean rejected = false;
    try {
        cart2.addItem(p1, -1);
    } catch (IllegalArgumentException e) {
        rejected = true;
    }

    System.out.println("Number of registered products: " + catalogService.getAllProducts().size());
    System.out.println("Number of registered customers: " + userService.getCustomerCount());
    System.out.println("Number of registered suppliers: " + userService.getSupplierCount());
    System.out.println("Cart subtotal before quantity/item changes: " + subtotalBeforeChange);
    System.out.println("Cart subtotal after the quantity change: " + subtotalAfterQuantityChange);
    System.out.println("Cart subtotal after the item removal: " + subtotalAfterRemoval);
    System.out.println("Order ORD1 subtotal: " + ord1.getSubtotal());
    System.out.println("Order ORD1 promotion discount: " + ord1.getPromotionDiscount());
    System.out.println("Order ORD1 shipping cost: " + ord1.getShippingCost());
    System.out.println("Order ORD1 final total: " + ord1.getFinalTotal());
    System.out.println("Order ORD1 final status: " + ord1.getStatus());
    System.out.println("Order ORD2 subtotal: " + ord2.getSubtotal());
    System.out.println("Order ORD2 promotion discount: " + ord2.getPromotionDiscount());
    System.out.println("Order ORD2 shipping cost: " + ord2.getShippingCost());
    System.out.println("Order ORD2 final total: " + ord2.getFinalTotal());
    System.out.println("Order ORD2 final status: " + ord2.getStatus());
    System.out.println("Return request identifier: " + returnRequest.getId());
    for (ReturnItem ri : returnRequest.getItems()) {
        System.out.println("Returned product: " + ri.getProduct().getId() + " Quantity: " + ri.getQuantity());
    }
    System.out.println("Refund total: " + returnRequest.getRefundTotal());
    System.out.println("Restock entry identifier: " + restock.getId());
    System.out.println("Restocked product: " + restock.getProduct().getId() + " Quantity: " + restock.getQuantity());
    System.out.println("Restock total cost: " + restock.getTotalCost());
    
    System.out.println("Final stock P1: " + inventoryService.getStock(p1));
    System.out.println("Final stock P2: " + inventoryService.getStock(p2));
    System.out.println("Final stock P3: " + inventoryService.getStock(p3));
    System.out.println("Final stock P4: " + inventoryService.getStock(p4));
    System.out.println("Final stock P5: " + inventoryService.getStock(p5));
    System.out.println("Invalid operation rejected: " + rejected);

    System.out.println("--- Stock Report ---");
    for (Product p : catalogService.getAllProducts()) {
        System.out.println(p.getId() + ": " + inventoryService.getStock(p));
    }

    System.out.println("--- Sales by Product Report ---");
    Map<String, Integer> sales = new HashMap<String, Integer>();
    for (Order o : orderService.getOrders()) {
        if (o.getStatus() == OrderStatus.PAID || o.getStatus() == OrderStatus.RETURNED) {
            for (OrderItem oi : o.getItems()) {
                Integer qty = sales.get(oi.getProduct().getId());
                if (qty == null) qty = 0;
                sales.put(oi.getProduct().getId(), qty + oi.getQuantity());
            }
        }
    }
    for (Product p : catalogService.getAllProducts()) {
        Integer sold = sales.get(p.getId());
        if (sold == null) sold = 0;
        System.out.println(p.getId() + ": " + sold);
    }

    System.out.println("--- Orders by Status Report ---");
    Map<OrderStatus, Integer> statusCounts = new HashMap<OrderStatus, Integer>();
    for (Order o : orderService.getOrders()) {
        Integer count = statusCounts.get(o.getStatus());
        if (count == null) count = 0;
        statusCounts.put(o.getStatus(), count + 1);
    }
    for (OrderStatus st : OrderStatus.values()) {
        Integer c = statusCounts.get(st);
        if (c == null) c = 0;
        System.out.println(st + ": " + c);
    }

    System.out.println("--- Cashflow Summary Report ---");
    System.out.println("Total Inflow: " + financeService.getTotalInflow());
    System.out.println("Total Outflow: " + financeService.getTotalOutflow());
    System.out.println("Net Cashflow: " + financeService.getNetCashflow());

    System.out.println("--- Returns/Refunds Report ---");
    for (ReturnRequest req : orderService.getReturns()) {
        System.out.println("Return ID: " + req.getId() + " Refund Total: " + req.getRefundTotal());
    }

    System.out.println("--- Restocking Report ---");
    for (Restock rst : inventoryService.getRestocks()) {
        System.out.println(rst.getId() + " | Product: " + rst.getProduct().getId() + " | Supplier: " + rst.getSupplier().getId() + " | Qty: " + rst.getQuantity() + " | Cost: " + rst.getTotalCost());
    }

    System.out.println("Exported: inventory.csv");
    System.out.println("Exported: cashflow.csv");
}
}