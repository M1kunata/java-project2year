/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project2_6713221;

import java.util.*;
import java.io.*;

/**
 *
 * @author 
 * 6713221 jakkarin roemtangsakul
 * 6713117 Nuttha Limkhunthammo
 * 6713115 Kornchanok Phutrakul
 */
class Warehouse {

    private int balance;
    private String name;

    public Warehouse() {
        this.balance = 0;
    }

    public void setname(String n) {
        name = n;
    }

    public String getname() {
        return name;
    }

    public synchronized int put(int amount) {
        if (amount > 0) {
            balance += amount;
        }
        return balance;
    }

    public synchronized int get(int requestAmount) {
        if (requestAmount <= 0) {
            return 0;
        }

        int actualAmount = Math.min(requestAmount, balance);
        balance -= actualAmount;

        return actualAmount;
    }

    public synchronized int getBalance() {
        return balance;
    }
}

class Freight {

    private final int maxCapacity;
    private int currentCapacity;
    private String name;

    public Freight(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentCapacity = maxCapacity;
    }

    public void set_name(String n) {
        name = n;
    }

    public String get_name() {
        return name;
    }

    public synchronized int ship(int requestAmount) {
        if (requestAmount <= 0) {
            return 0;
        }

        int actualAmount = Math.min(requestAmount, currentCapacity);
        currentCapacity -= actualAmount;

        return actualAmount;
    }

    public synchronized void reset() {
        currentCapacity = maxCapacity;
    }

    public synchronized int getCurrentCapacity() {
        return currentCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}

class Config {

    private int days;

    private int warehouseNum;

    private int freightNum;
    private int freightMaxCapacity;

    private int supplierNum;
    private int supplierMin;
    private int supplierMax;

    private int factoryNum;
    private int factoryMax;

    public Config() {
        this.days = 0;
        this.warehouseNum = 0;
        this.freightNum = 0;
        this.freightMaxCapacity = 0;
        this.supplierNum = 0;
        this.supplierMin = 0;
        this.supplierMax = 0;
        this.factoryNum = 0;
        this.factoryMax = 0;
    }

    public int getdays() {
        return days;
    }

    public int getwarehouseNum() {
        return warehouseNum;
    }

    public int getfreightNum() {
        return freightNum;
    }

    public int getfreightMaxCapacity() {
        return freightMaxCapacity;
    }

    public int getsupplierNum() {
        return supplierNum;
    }

    public int getsupplierMin() {
        return supplierMin;
    }

    public int getsupplierMax() {
        return supplierMax;
    }

    public int getfactoryNum() {
        return factoryNum;
    }

    public int getfactoryMax() {
        return factoryMax;
    }

    public void display() {
        System.out.println("=== Configuration ===");
        System.out.println("Days: " + days);
        System.out.println("Warehouses: " + warehouseNum);
        System.out.println("Freights: " + freightNum + " (max capacity: " + freightMaxCapacity + ")");
        System.out.println("Suppliers: " + supplierNum + " (supply: " + supplierMin + "-" + supplierMax + ")");
        System.out.println("Factories: " + factoryNum + " (max production: " + factoryMax + ")");
        System.out.println("=====================\n");
    }

    public boolean isValid() {
        return days > 0 && warehouseNum > 0 && freightNum > 0
                && freightMaxCapacity > 0 && supplierNum > 0
                && supplierMin > 0 && supplierMax >= supplierMin
                && factoryNum > 0 && factoryMax > 0;
    }

    public static Config readConfig() {
        while (true) {
            String path = "src/main/java/Project2_6713221/";
            System.out.print("New file name = ");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            String filename = path + input;

            try {
                File inFile = new File(filename);
                Scanner fileScan = new Scanner(inFile);

                Config config = new Config();

                while (fileScan.hasNextLine()) {
                    String line = fileScan.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    try {
                        String[] parts = line.split(",");
                        if (parts.length < 2) {
                            continue;
                        }

                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].trim();
                        }

                        String key = parts[0];

                        switch (key) {
                            case "days":
                                config.days = Integer.parseInt(parts[1]);
                                break;

                            case "warehouse_num":
                                config.warehouseNum = Integer.parseInt(parts[1]);
                                break;

                            case "freight_num_max":
                                if (parts.length >= 3) {
                                    config.freightNum = Integer.parseInt(parts[1]);
                                    config.freightMaxCapacity = Integer.parseInt(parts[2]);
                                }
                                break;

                            case "supplier_num_min_max":
                                if (parts.length >= 4) {
                                    config.supplierNum = Integer.parseInt(parts[1]);
                                    config.supplierMin = Integer.parseInt(parts[2]);
                                    config.supplierMax = Integer.parseInt(parts[3]);
                                }
                                break;

                            case "factory_num_max":
                                if (parts.length >= 3) {
                                    config.factoryNum = Integer.parseInt(parts[1]);
                                    config.factoryMax = Integer.parseInt(parts[2]);
                                }
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(Thread.currentThread().getName()
                                + ": Error parsing line: " + line);
                        System.out.println(Thread.currentThread().getName()
                                + ": " + e.getMessage());
                    }
                }

                fileScan.close();

                if (!config.isValid()) {
                    System.out.println("Error - Invalid configuration values. Please try again.");
                    continue;
                }

                return config;

            } catch (FileNotFoundException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }

}

public class readdata {

    public static void main(String[] args) {
        System.out.println("=== Testing Person 1's Classes ===\n");

        testWarehouse();

        testFreight();

        System.out.println("\n=== All Tests Complete ===");
    }

    private static void testWarehouse() {
        System.out.println("--- Testing Warehouse ---");

        Warehouse wh = new Warehouse();
        System.out.println("Initial balance: " + wh.getBalance());
        wh.put(100);
        System.out.println("After put(100): " + wh.getBalance()); 

        wh.put(50);
        System.out.println("After put(50): " + wh.getBalance()); 

        int got = wh.get(80);
        System.out.println("Get(80) returned: " + got);
        System.out.println("Balance after get(80): " + wh.getBalance());

        got = wh.get(100);
        System.out.println("Get(100) returned: " + got);
        System.out.println("Balance after get(100): " + wh.getBalance());

        got = wh.get(50);
        System.out.println("Get(50) from empty warehouse: " + got);

        System.out.println("✓ Warehouse test passed\n");
    }

    private static void testFreight() {
        System.out.println("--- Testing Freight ---");

        Freight freight = new Freight(200);
        System.out.println("Max capacity: " + freight.getMaxCapacity());
        System.out.println("Initial current capacity: " + freight.getCurrentCapacity());

        int shipped = freight.ship(80);
        System.out.println("Ship(80) returned: " + shipped);
        System.out.println("Capacity after ship(80): " + freight.getCurrentCapacity());

        shipped = freight.ship(150);
        System.out.println("Ship(150) returned: " + shipped);
        System.out.println("Capacity after ship(150): " + freight.getCurrentCapacity());

        shipped = freight.ship(50);
        System.out.println("Ship(50) when full: " + shipped);

        freight.reset();
        System.out.println("After reset(): " + freight.getCurrentCapacity());

        System.out.println("✓ Freight test passed\n");
    }
}
