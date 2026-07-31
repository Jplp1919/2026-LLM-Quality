package store;

import store.model.Cart;
import store.model.CashflowEntry;
import store.model.CashflowType;
import store.model.Category;
import store.model.Customer;
import store.model.LoyaltyTier;
import store.model.Order;
import store.model.Product;
import store.model.Restock;
import store.model.ReturnRequest;
import store.model.Supplier;
import store.promotion.BuyXGetYPromotion;
import store.promotion.FixedTierPromotion;
import store.promotion.PercentagePromotion;
import store.promotion.Promotion;
import store.service.CsvExporter;
import store.service.StoreSystem;
import store.shipping.ExpressShipping;
import store.shipping.PickupShipping;
import store.shipping.StandardShipping;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        StoreSystem system = new StoreSystem();

        Category c1 = new Category("C1", "Office");
        Category c2 = new Category("C2", "Accessories");
        Category c3 = new Category("C3", "Bags");
        system.registerCategory(c1);
        system.registerCategory(c2);
        system.registerCategory(c3);

        Product p1 = new Product("P1", "Notebook", 15.0, c1);
        Product p2 = new Product("P2", "Pen", 5.0, c1);
        Product p3 = new Product("P3", "Mug", 20.0, c2);
        Product p4 = new Product("P4", "Backpack", 80.0, c3);
        Product p5 = new Product("P5", "Bottle", 25.0, c2);
        
        system.registerProduct(p1);
        system.registerProduct(p2);
        system.registerProduct(p3);
        system.registerProduct(p4);
        system.registerProduct(p5);

        system.increaseStock("P1", 100);
        system.increaseStock("P2", 200);
        system.increaseStock("P3", 40);
        system.increaseStock("P4", 15);
        system.increaseStock("P5", 30);

        Customer cu1 = new Customer("CU1", "Ana", LoyaltyTier.REGULAR);
        Customer cu2 = new Customer("CU2", "Bruno", LoyaltyTier.GOLD);
        Customer cu3 = new Customer("CU3", "Carla", LoyaltyTier.SILVER);
        system.registerCustomer(cu1);
        system.registerCustomer(cu2);
        system.registerCustomer(cu3);

        Supplier su1 = new Supplier("SU1", "Alpha Supplies");
        Supplier su2 = new Supplier("SU2", "Beta Wholesale");
        system.registerSupplier(su1);
        system.registerSupplier(su2);

        Promotion pctPromo = new PercentagePromotion(10.0, 200.0);
        Promotion fixedPromo = new FixedTierPromotion(LoyaltyTier.GOLD, 50.0, 5.0);
        Promotion bogoPromo = new BuyXGetYPromotion(p2, 2, 1);

        StandardShipping standardShipping = new StandardShipping(12.0);
        ExpressShipping expressShipping = new ExpressShipping(25.0);
        PickupShipping pickupShipping = new PickupShipping();

        Cart cart1 = system.createCart(cu2);
        system.addToCart(cart1, p4, 1);
        system.addToCart(cart1, p1, 1);
        system.addToCart(cart1, p2, 3);
        system.addToCart(cart1, p5, 1);
        
        System.out.println("Registered products: " + system.getProducts().size());
        System.out.println("Registered customers: " + system.getCustomers().size());
        System.out.println("Registered suppliers: " + system.getSuppliers().size());
        System.out.println("Cart subtotal before quantity/item changes: " + cart1.getSubtotal());

        system.updateCartItem(cart1, p1, 2);
        System.out.println("Cart subtotal after the quantity change: " + cart1.getSubtotal());

        system.removeFromCart(cart1, p5);
        System.out.println("Cart subtotal after the item removal: " + cart1.getSubtotal());

        Order ord1 = system.createOrder("ORD1", cart1);
        system.applyPromotion(ord1, bogoPromo);
        system.applyShipping(ord1, standardShipping);

        System.out.println("Order ORD1 subtotal: " + ord1.getSubtotal());
        System.out.println("Order ORD1 promotion discount: " + ord1.getDiscount());
        System.out.println("Order ORD1 shipping cost: " + ord1.getShipping());
        System.out.println("Order ORD1 final total: " + ord1.getTotal());
        
        system.payOrder(ord1);
        System.out.println("Order ORD1 final status: " + ord1.getStatus());

        Cart cart2 = system.createCart(cu1);
        system.addToCart(cart2, p3, 2);
        system.addToCart(cart2, p5, 1);
        
        Order ord2 = system.createOrder("ORD2", cart2);
        system.applyShipping(ord2, pickupShipping);
        
        System.out.println("Order ORD2 subtotal: " + ord2.getSubtotal());
        System.out.println("Order ORD2 promotion discount: " + ord2.getDiscount());
        System.out.println("Order ORD2 shipping cost: " + ord2.getShipping());
        System.out.println("Order ORD2 final total: " + ord2.getTotal());
        
        system.cancelOrder(ord2);
        System.out.println("Order ORD2 final status: " + ord2.getStatus());

        Map<Product, Integer> returnedItems = new HashMap<Product, Integer>();
        returnedItems.put(p4, 1);
        ReturnRequest ret1 = system.createReturn("RET1", ord1, returnedItems);
        
        System.out.println("Return request identifier: " + ret1.getId());
        System.out.println("Returned product identifiers and quantities: P4 x 1");
        System.out.println("Refund total: " + ret1.getRefundTotal());

        Restock rsk1 = system.registerRestock("RSK1", p4, su1, 10, 40.0);
        System.out.println("Restock entry identifier: " + rsk1.getId());
        System.out.println("Restocked product identifier and quantity: " + rsk1.getProduct().getId() + " x " + rsk1.getQuantity());
        System.out.println("Restock total cost: " + rsk1.getTotalCost());

        system.registerCashflowEntry(new CashflowEntry("CF_MANUAL_1", CashflowType.OUTFLOW, 15.0, "Office supplies"));

        System.out.println("Final stock values of P1: " + system.getStock("P1"));
        System.out.println("Final stock values of P2: " + system.getStock("P2"));
        System.out.println("Final stock values of P3: " + system.getStock("P3"));
        System.out.println("Final stock values of P4: " + system.getStock("P4"));
        System.out.println("Final stock values of P5: " + system.getStock("P5"));

        try {
            system.payOrder(ord2);
            System.out.println("Invalid operation was not rejected!");
        } catch (BusinessException e) {
            System.out.println("Confirmation that the invalid operation was rejected: " + e.getMessage());
        }

        CsvExporter exporter = new CsvExporter();
        exporter.exportInventory(system.getProducts(), system.getStockMap());
        exporter.exportCashflow(system.getCashflow());

        system.printStockReport();
        system.printSalesByProductReport();
        system.printOrdersByStatusReport();
        system.printCashflowSummaryReport();
        system.printReturnsReport();
        system.printRestockingReport();
    }
}