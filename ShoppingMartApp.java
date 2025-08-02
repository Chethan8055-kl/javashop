import java.sql.*;
import java.util.Scanner;

public class ShoppingMartApp {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Shopping Mart Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Purchase Product");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> viewProducts();
                case 3 -> purchaseProduct();
                case 4 -> {
                    System.out.println("Thank you for visiting!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    static void addProduct() {
        System.out.print("Product Name: ");
        String name = sc.next();
        System.out.print("Price: ");
        double price = sc.nextDouble();
        System.out.print("Quantity: ");
        int quantity = sc.nextInt();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO products(name, price, quantity) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void viewProducts() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
            System.out.printf("%-5s %-15s %-10s %-10s%n", "ID", "Name", "Price", "Qty");
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-10.2f %-10d%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void purchaseProduct() {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();
        System.out.print("Enter Quantity to Purchase: ");
        int qty = sc.nextInt();

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT quantity, price FROM products WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int available = rs.getInt("quantity");
                double price = rs.getDouble("price");
                if (qty <= available) {
                    double total = qty * price;
                    PreparedStatement updatePs = con.prepareStatement("UPDATE products SET quantity=? WHERE id=?");
                    updatePs.setInt(1, available - qty);
                    updatePs.setInt(2, id);
                    updatePs.executeUpdate();
                    System.out.println("Purchase successful! Total: ₹" + total);
                } else {
                    System.out.println("Only " + available + " items available.");
                }
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}