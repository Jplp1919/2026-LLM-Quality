package store.service;

import store.BusinessException;
import store.model.Cart;
import store.model.CashflowEntry;
import store.model.CashflowType;
import store.model.Category;
import store.model.Customer;
import store.model.Order;
import store.model.OrderItem;
import store.model.OrderStatus;
import store.model.Product;
import store.model.Restock;
import store.model.ReturnItem;
import store.model.ReturnRequest;
import store.model.Supplier;
import store.promotion.Promotion;
import store.shipping.ShippingRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StoreSystem {
    private Map<String, Category> categories = new HashMap<String, Category>();
    private Map<String, Product> products = new HashMap<String, Product>();
    private Map<String, Customer> customers = new HashMap<String, Customer>();
    private Map<String, Supplier> suppliers = new HashMap<String, Supplier>();
    private Map<String, Integer> stock = new HashMap<String, Integer>();
    private List<Order> orders = new ArrayList<Order>();
    private List<CashflowEntry> cashflow = new ArrayList<CashflowEntry>();
    private List<ReturnRequest> returns = new ArrayList<ReturnRequest>();
    private List<Restock> restocks = new ArrayList<Restock>();

    public void registerCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public void registerProduct(Product product) {
        if (products.containsKey(product.getId())) {
            throw new BusinessException("Product identifier must be unique.");
        }
        if (product.getUnitPrice() < 0) {
            throw new BusinessException("Unit price cannot be negative.");
        }
        products.put(product.getId(), product);
        stock.put(product.getId(), 0);
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public List<Product> searchProductsByName(String name) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getName().contains(name)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> listProductsByCategory(String categoryId) {
        List<Product> result = new ArrayList<Product>();
        for (Product p : products.values()) {
            if (p.getCategory().getId().equals(categoryId)) {
                result.add(p);
            }
        }
        return result;
    }

    public void setProductActive(String productId, boolean active) {
        Product p = products.get(productId);
        if (p != null) {
            p.setActive(active);
        }
    }

    public void registerCustomer(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            throw new BusinessException("Customer identifier must be unique.");
        }
        customers.put(customer.getId(), customer);
    }

    public void registerSupplier(Supplier supplier) {
        if (suppliers.containsKey(supplier.getId())) {
            throw new BusinessException("Supplier identifier must be unique.");
        }
        suppliers.put(supplier.getId(), supplier);
    }

    public void increaseStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new BusinessException("Cannot increase stock by a negative amount.");
        }
        int current = stock.get(productId);
        stock.put(productId, current + quantity);
    }

    public void decreaseStock(String productId, int quantity) {
        if (quantity < 0) {
            throw new BusinessException("Cannot decrease stock by a negative amount.");
        }
        int current = stock.get(productId);
        if (current - quantity < 0) {
            throw new BusinessException("Stock quantities cannot become negative.");
        }
        stock.put(productId, current - quantity);
    }

    public int getStock(String productId) {
        return stock.get(productId);
    }

    public Cart createCart(Customer customer) {
        return new Cart(customer);
    }

    public void addToCart(Cart cart, Product product, int quantity) {
        if (!product.isActive()) {
            throw new BusinessException("Inactive products cannot be added to carts.");
        }
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantities must be positive.");
        }
        Integer current = cart.getItems().get(product);
        if (current == null) {
            current = 0;
        }
        cart.getItems().put(product, current + quantity);
    }

    public void updateCartItem(Cart cart, Product product, int quantity) {
        if (quantity <= 0) {
            throw new BusinessException("Cart item quantities must be positive.");
        }
        if (cart.getItems().containsKey(product)) {
            cart.getItems().put(product, quantity);
        }
    }

    public void removeFromCart(Cart cart, Product product) {
        cart.getItems().remove(product);
    }

    public Order createOrder(String orderId, Cart cart) {
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Each order must contain at least one order item.");
        }
        
        List<OrderItem> items = new ArrayList<OrderItem>();
        double subtotal = 0.0;
        
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            decreaseStock(product.getId(), quantity);
            
            OrderItem orderItem = new OrderItem(product, quantity, product.getUnitPrice());
            items.add(orderItem);
            subtotal += orderItem.getSubtotal();
        }
        
        Order order = new Order(orderId, cart.getCustomer(), items, subtotal);
        orders.add(order);
        cart.getItems().clear();
        return order;
    }

    public void applyPromotion(Order order, Promotion promotion) {
        double discount = promotion.apply(order);
        if (discount < 0) {
            throw new BusinessException("Discount values cannot be negative.");
        }
        order.setDiscount(discount);
    }

    public void applyShipping(Order order, ShippingRule shippingRule) {
        double cost = shippingRule.calculate(order);
        if (cost < 0) {
            throw new BusinessException("Shipping values cannot be negative.");
        }
        order.setShipping(cost);
    }

    public void payOrder(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessException("Cancelled orders cannot be paid.");
        }
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.RETURNED) {
            throw new BusinessException("Order is already paid or returned.");
        }
        if (order.getTotal() < 0) {
            throw new BusinessException("Payment values cannot be negative.");
        }
        
        order.setStatus(OrderStatus.PAID);
        registerCashflowEntry(new CashflowEntry(UUID.randomUUID().toString(), CashflowType.INFLOW, order.getTotal(), "Payment for order " + order.getId()));
    }

    public void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.PAID || order.getStatus() == OrderStatus.RETURNED) {
            throw new BusinessException("Paid orders cannot be cancelled.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        for (OrderItem item : order.getItems()) {
            increaseStock(item.getProduct().getId(), item.getQuantity());
        }
    }

    public ReturnRequest createReturn(String returnId, Order order, Map<Product, Integer> returnedProducts) {
        if (order.getStatus() != OrderStatus.PAID && order.getStatus() != OrderStatus.RETURNED) {
            throw new BusinessException("Only paid orders can be returned.");
        }
        if (returnedProducts.isEmpty()) {
            throw new BusinessException("A return must contain at least one returned item.");
        }
        
        List<ReturnItem> returnItems = new ArrayList<ReturnItem>();
        double refundTotal = 0.0;
        
        for (Map.Entry<Product, Integer> entry : returnedProducts.entrySet()) {
            Product p = entry.getKey();
            int qtyToReturn = entry.getValue();
            
            int boughtQty = 0;
            double unitPrice = 0.0;
            for (OrderItem oi : order.getItems()) {
                if (oi.getProduct().getId().equals(p.getId())) {
                    boughtQty += oi.getQuantity();
                    unitPrice = oi.getUnitPrice();
                }
            }
            
            if (qtyToReturn > boughtQty) {
                throw new BusinessException("The returned quantity of a product cannot exceed the quantity originally bought in that order.");
            }
            
            returnItems.add(new ReturnItem(p, qtyToReturn));
            refundTotal += qtyToReturn * unitPrice;
        }
        
        if (refundTotal < 0) {
            throw new BusinessException("Refund values cannot be negative.");
        }
        
        for (ReturnItem ri : returnItems) {
            increaseStock(ri.getProduct().getId(), ri.getQuantity());
        }
        
        ReturnRequest req = new ReturnRequest(returnId, order, returnItems, refundTotal);
        returns.add(req);
        
        registerCashflowEntry(new CashflowEntry(UUID.randomUUID().toString(), CashflowType.OUTFLOW, refundTotal, "Refund for return " + returnId));
        order.setStatus(OrderStatus.RETURNED);
        
        return req;
    }

    public Restock registerRestock(String restockId, Product product, Supplier supplier, int quantity, double unitCost) {
        if (quantity <= 0) {
            throw new BusinessException("Restock quantities must be positive.");
        }
        if (unitCost < 0) {
            throw new BusinessException("Restock unit costs cannot be negative.");
        }
        
        Restock restock = new Restock(restockId, product, supplier, quantity, unitCost);
        restocks.add(restock);
        
        increaseStock(product.getId(), quantity);
        registerCashflowEntry(new CashflowEntry(UUID.randomUUID().toString(), CashflowType.OUTFLOW, restock.getTotalCost(), "Restock " + restockId));
        
        return restock;
    }

    public void registerCashflowEntry(CashflowEntry entry) {
        cashflow.add(entry);
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, Supplier> getSuppliers() {
        return suppliers;
    }

    public List<CashflowEntry> getCashflow() {
        return cashflow;
    }

    public Map<String, Integer> getStockMap() {
        return stock;
    }

    public void printStockReport() {
        System.out.println("--- Stock Report ---");
        for (Product p : products.values()) {
            System.out.println(p.getId() + ": " + stock.get(p.getId()));
        }
    }

    public void printSalesByProductReport() {
        System.out.println("--- Sales by Product Report ---");
        Map<String, Integer> sales = new HashMap<String, Integer>();
        for (Product p : products.values()) {
            sales.put(p.getId(), 0);
        }
        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.PAID || o.getStatus() == OrderStatus.RETURNED) {
                for (OrderItem item : o.getItems()) {
                    int current = sales.get(item.getProduct().getId());
                    sales.put(item.getProduct().getId(), current + item.getQuantity());
                }
            }
        }
        for (Map.Entry<String, Integer> entry : sales.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " sold");
        }
    }

    public void printOrdersByStatusReport() {
        System.out.println("--- Orders by Status Report ---");
        Map<OrderStatus, Integer> counts = new HashMap<OrderStatus, Integer>();
        for (OrderStatus s : OrderStatus.values()) {
            counts.put(s, 0);
        }
        for (Order o : orders) {
            counts.put(o.getStatus(), counts.get(o.getStatus()) + 1);
        }
        for (Map.Entry<OrderStatus, Integer> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printCashflowSummaryReport() {
        System.out.println("--- Cashflow Summary Report ---");
        double inflow = 0.0;
        double outflow = 0.0;
        for (CashflowEntry e : cashflow) {
            if (e.getType() == CashflowType.INFLOW) {
                inflow += e.getAmount();
            } else {
                outflow += e.getAmount();
            }
        }
        System.out.println("Total Inflow: " + inflow);
        System.out.println("Total Outflow: " + outflow);
        System.out.println("Net Cashflow: " + (inflow - outflow));
    }

    public void printReturnsReport() {
        System.out.println("--- Returns & Refunds Report ---");
        for (ReturnRequest r : returns) {
            System.out.println("Return ID: " + r.getId() + ", Refund Total: " + r.getRefundTotal());
            for (ReturnItem ri : r.getItems()) {
                System.out.println("  - " + ri.getProduct().getId() + " x" + ri.getQuantity());
            }
        }
    }

    public void printRestockingReport() {
        System.out.println("--- Restocking Report ---");
        for (Restock r : restocks) {
            System.out.println("Restock ID: " + r.getId() + ", Product: " + r.getProduct().getId() + ", Supplier: " + r.getSupplier().getId() + ", Qty: " + r.getQuantity() + ", Total Cost: " + r.getTotalCost());
        }
    }
}