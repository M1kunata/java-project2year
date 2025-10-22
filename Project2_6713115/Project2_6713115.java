/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project2_6713115;

import java.util.*;
import java.io.*;  
/**
 *
 * @author 6713115 kornchanok phutrakul
 */
class Warehouse {
    private int balance;
    public Warehouse() {
        this.balance = 0;
    }
    public synchronized void put(int amount) {
        if (amount > 0) {
            balance += amount;
        }
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

    public Freight(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.currentCapacity = maxCapacity;
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
    public int days;

    public int warehouseNum;

    public int freightNum;
    public int freightMaxCapacity;

    public int supplierNum;
    public int supplierMin;
    public int supplierMax;

    public int factoryNum;
    public int factoryMax;

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
        return days > 0 && warehouseNum > 0 && freightNum > 0 && 
               freightMaxCapacity > 0 && supplierNum > 0 && 
               supplierMin > 0 && supplierMax >= supplierMin && 
               factoryNum > 0 && factoryMax > 0;
    }
}

class ConfigReader {
    
    public static Config readConfig(String filename) {
        Config config = new Config();
        
        try {
            File inFile = new File(filename);
            Scanner fileScan = new Scanner(inFile);
            
            System.out.println(Thread.currentThread().getName() + 
                ": Read config from (relative path) " + inFile.getPath());
            System.out.println(Thread.currentThread().getName() + 
                ": Read config from (absolute path) " + inFile.getAbsolutePath() + "\n");
            
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();

                if (line.trim().isEmpty()) {
                    continue;
                }

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
                        
                    default:
                        break;
                }
            }
            
            fileScan.close();

            if (!config.isValid()) {
                System.out.println(Thread.currentThread().getName() + 
                    ": Error - Invalid configuration values in " + filename);
                return null;
            }
            
            System.out.println(Thread.currentThread().getName() + 
                ": Successfully read configuration.\n");
            
            return config;
            
        } catch (FileNotFoundException e) {
            System.out.println(Thread.currentThread().getName() + 
                ": Error - Configuration file '" + filename + "' not found!");
            System.out.println(Thread.currentThread().getName() + 
                ": Please make sure config.txt exists in the correct folder.");
            return null;
            
        } catch (NumberFormatException e) {
            System.out.println(Thread.currentThread().getName() + 
                ": Error - Invalid number format in configuration file: " + e.getMessage());
            return null;
        }
    }

    public static Config readConfig() {
        return readConfig("src/main/Java/Project2_6713115/config_1.txt");
    }
}

/*
 * This will be replaced by the actual Main class from Mixja
 */
public class Project2_6713115 {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Person 1's Classes ===\n");
        
        // Test Warehouse
        testWarehouse();
        
        // Test Freight
        testFreight();
        
        // Test ConfigReader
        testConfigReader();
        
        System.out.println("\n=== All Tests Complete ===");
    }
    
    /**
     * Test Warehouse class functionality
     */
    private static void testWarehouse() {
        System.out.println("--- Testing Warehouse ---");
        
        Warehouse wh = new Warehouse();
        System.out.println("Initial balance: " + wh.getBalance()); // Should be 0
        
        // Test put
        wh.put(100);
        System.out.println("After put(100): " + wh.getBalance()); // Should be 100
        
        wh.put(50);
        System.out.println("After put(50): " + wh.getBalance()); // Should be 150
        
        // Test get - normal case
        int got = wh.get(80);
        System.out.println("Get(80) returned: " + got); // Should be 80
        System.out.println("Balance after get(80): " + wh.getBalance()); // Should be 70
        
        // Test get - insufficient balance
        got = wh.get(100);
        System.out.println("Get(100) returned: " + got); // Should be 70 (all remaining)
        System.out.println("Balance after get(100): " + wh.getBalance()); // Should be 0
        
        // Test get when empty
        got = wh.get(50);
        System.out.println("Get(50) from empty warehouse: " + got); // Should be 0
        
        System.out.println("✓ Warehouse test passed\n");
    }
    
    /**
     * Test Freight class functionality
     */
    private static void testFreight() {
        System.out.println("--- Testing Freight ---");
        
        Freight freight = new Freight(200);
        System.out.println("Max capacity: " + freight.getMaxCapacity()); // Should be 200
        System.out.println("Initial current capacity: " + freight.getCurrentCapacity()); // Should be 200
        
        // Test ship - normal case
        int shipped = freight.ship(80);
        System.out.println("Ship(80) returned: " + shipped); // Should be 80
        System.out.println("Capacity after ship(80): " + freight.getCurrentCapacity()); // Should be 120
        
        // Test ship - exceeds capacity
        shipped = freight.ship(150);
        System.out.println("Ship(150) returned: " + shipped); // Should be 120 (all remaining)
        System.out.println("Capacity after ship(150): " + freight.getCurrentCapacity()); // Should be 0
        
        // Test ship when full
        shipped = freight.ship(50);
        System.out.println("Ship(50) when full: " + shipped); // Should be 0
        
        // Test reset
        freight.reset();
        System.out.println("After reset(): " + freight.getCurrentCapacity()); // Should be 200
        
        System.out.println("✓ Freight test passed\n");
    }
    
    /**
     * Test ConfigReader class functionality
     */
    private static void testConfigReader() {
        System.out.println("--- Testing ConfigReader ---");
        
        // Test with default path
        Config config = ConfigReader.readConfig("src/main/Java/Project2_6713115/config_1.txt");
        
        if (config != null) {
            System.out.println("✓ Config file loaded successfully");
            config.display();
            
            // Verify values
            System.out.println("Validating config values...");
            System.out.println("Days > 0: " + (config.days > 0));
            System.out.println("Warehouses > 0: " + (config.warehouseNum > 0));
            System.out.println("Freights > 0: " + (config.freightNum > 0));
            System.out.println("Suppliers > 0: " + (config.supplierNum > 0));
            System.out.println("Factories > 0: " + (config.factoryNum > 0));
            System.out.println("Valid config: " + config.isValid());
            
        } else {
            System.out.println("✗ Failed to load config file");
            System.out.println("Note: Make sure config.txt exists in Project2_XXX folder");
            System.out.println("\nSample config.txt content:");
            System.out.println("days, 5");
            System.out.println("warehouse_num, 3");
            System.out.println("freight_num_max, 2, 100");
            System.out.println("supplier_num_min_max, 3, 50, 100");
            System.out.println("factory_num_max, 3, 80");
        }
        
        System.out.println("✓ ConfigReader test complete\n");
    }
}