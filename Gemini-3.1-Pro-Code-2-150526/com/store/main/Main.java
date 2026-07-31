package com.store.main;

import com.store.domain.Cart;
import com.store.domain.Category;
import com.store.domain.Customer;
import com.store.domain.Order;
import com.store.domain.Product;
import com.store.domain.RestockEntry;
import com.store.domain.ReturnItem;
import com.store.domain.ReturnRequest;
import com.store.domain.Supplier;
import com.store.domain.enums.CustomerTier;
import com.store.export.CsvExporter;
import com.store.promotions.BuyXGetYPromotion;
import com.store.promotions.FixedDiscountPromotion;
import com.store.promotions.PercentagePromotion;
import com.store.promotions.Promotion;
import com.store.service.CashflowService;
import com.store.service.CatalogService;
import com.store.service.CustomerService;
import com.store.service.InventoryService;
import com.store.service.OrderService;
import com.store.service.ReportingService;
import com.store.shipping.ExpressShipping;
import com.store.shipping.PickupShipping;
import com.store.shipping.ShippingRule;
import com.store.shipping.StandardShipping;

public class Main {
    public static void main(String[] args) {
        try {
            CatalogService catalog = new CatalogService();
            CashflowService cashflow = new CashflowService();
            InventoryService inventory = new InventoryService(cashflow);
            CustomerService customers = new CustomerService();
            OrderService orders = new OrderService(inventory, cashflow);
            ReportingService reports = new ReportingService(inventory, orders, cashflow);

            Category c1 = new Category("C1", "Office");
            Category c2 = new Category("C2", "Accessories");
            Category c3 = new Category("C3", "Bags");
            catalog.addCategory(c1);
            catalog.addCategory(c2);
            catalog.addCategory(c3);

            Product p1 = new Product("P1", "Notebook", 15.0, c1);
            Product p2 = new Product("P2", "Pen", 5.0, c1);
            Product p3 = new Product("P3", "Mug", 20.0, c2);
            Product p4 = new Product("P4", "Backpack", 80.0, c3);
            Product p5 = new Product("P5", "Bottle", 25.0, c2);
            catalog.addProduct(p1);
            catalog.addProduct(p2);
            catalog.addProduct(p3);
            catalog.addProduct(p4);
            catalog.addProduct(p5);

            inventory.initializeStock("P1", 100);
            inventory.initializeStock("P2", 200);
            inventory.initializeStock("P3", 40);
            inventory.initializeStock("P4", 15);
            inventory.initializeStock("P5", 30);

            Customer cu1 = new Customer("CU1", "Ana", CustomerTier.REGULAR);
            Customer cu2 = new Customer("CU2", "Bruno", CustomerTier.GOLD);
            Customer cu3 = new Customer("CU3", "Carla", CustomerTier.SILVER);
            customers.registerCustomer(cu1);
            customers.registerCustomer(cu2);
            customers.registerCustomer(cu3);

            Supplier su1 = new Supplier("SU1", "Alpha Supplies");
            Supplier su2 = new Supplier("SU2", "Beta Wholesale");
            customers.registerSupplier(su1);
            customers.registerSupplier(su2);

            Promotion promoPercentage = new PercentagePromotion(200.0, 10.0);
            Promotion promoFixed = new FixedDiscountPromotion(5.0, 50.0, CustomerTier.GOLD);
            Promotion promoBogo = new BuyXGetYPromotion(p2, 2, 1);

            ShippingRule shipStandard = new StandardShipping(12.0);
            ShippingRule shipExpress = new ExpressShipping(25.0);
            ShippingRule shipPickup = new PickupShipping();

            System.out.println("Registered Products: " + catalog.getProductCount());
            System.out.println("Registered Customers: " + customers.getCustomerCount());
            System.out.println("Registered Suppliers: " + customers.getSupplierCount());

            Cart cart2 = new Cart(cu2);
            cart2.addProduct(p4, 1);
            cart2.addProduct(p1, 2);
            cart2.addProduct(p2, 3);
            cart2.addProduct(p5, 1);
            System.out.println("Cart subtotal before changes: " + cart2.getSubtotalPreview());

            cart2.updateQuantity("P1", 3);
            System.out.println("Cart subtotal after quantity change: " + cart2.getSubtotalPreview());

            cart2.removeProduct("P5");
            System.out.println("Cart subtotal after item removal: " + cart2.getSubtotalPreview());

            Order ord1 = orders.createOrder("ORD1", cart2);
            orders.applyPromotion(ord1, promoFixed);
            orders.applyShipping(ord1, shipStandard);
            orders.payOrder(ord1);

            System.out.println("Order ORD1 subtotal: " + ord1.getSubtotal());
            System.out.println("Order ORD1 promotion discount: " + ord1.getPromotionDiscount());
            System.out.println("Order ORD1 shipping cost: " + ord1.getShippingCost());
            System.out.println("Order ORD1 final total: " + ord1.getFinalTotal());
            System.out.println("Order ORD1 final status: " + ord1.getStatus().name());

            Cart cart1 = new Cart(cu1);
            cart1.addProduct(p3, 2);
            cart1.addProduct(p4, 1);
            Order ord2 = orders.createOrder("ORD2", cart1);
            orders.applyShipping(ord2, shipExpress);

            System.out.println("Order ORD2 subtotal: " + ord2.getSubtotal());
            System.out.println("Order ORD2 promotion discount: " + ord2.getPromotionDiscount());
            System.out.println("Order ORD2 shipping cost: " + ord2.getShippingCost());
            System.out.println("Order ORD2 final total: " + ord2.getFinalTotal());
            
            orders.cancelOrder(ord2);
            System.out.println("Order ORD2 final status: " + ord2.getStatus().name());

            ReturnRequest retReq = orders.createReturnRequest(ord1);
            retReq.addItem(p2, 1);
            orders.processReturn(retReq);

            System.out.println("Return request identifier: " + retReq.getId());
            for (ReturnItem ri : retReq.getItems()) {
                System.out.println("Returned product identifier: " + ri.getProduct().getId() + " | Quantity: " + ri.getQuantity());
            }
            System.out.println("Refund total: " + retReq.getRefundTotal());

            RestockEntry rst = inventory.restock(p1, su1, 50, 10.0);
            System.out.println("Restock entry identifier: " + rst.getId());
            System.out.println("Restocked product identifier: " + rst.getProduct().getId() + " | Quantity: " + rst.getQuantity());
            System.out.println("Restock total cost: " + rst.getTotalCost());

            cashflow.registerOutflow("MANUAL_01", 100.0, "Manual outflow entry");

            System.out.println("Final stock P1: " + inventory.getStock("P1"));
            System.out.println("Final stock P2: " + inventory.getStock("P2"));
            System.out.println("Final stock P3: " + inventory.getStock("P3"));
            System.out.println("Final stock P4: " + inventory.getStock("P4"));
            System.out.println("Final stock P5: " + inventory.getStock("P5"));

            try {
                orders.cancelOrder(ord1);
            } catch (Exception e) {
                System.out.println("Invalid operation rejected successfully: " + e.getMessage());
            }

            reports.printStockReport();
            reports.printSalesReport();
            reports.printOrdersByStatusReport();
            reports.printCashflowSummaryReport();
            reports.printReturnsReport();
            reports.printRestockingReport();

            String invFile = "inventory.csv";
            CsvExporter.exportInventory(inventory, invFile);
            System.out.println("Exported inventory to " + invFile);

            String cashFile = "cashflow.csv";
            CsvExporter.exportCashflow(cashflow, cashFile);
            System.out.println("Exported cashflow to " + cashFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}