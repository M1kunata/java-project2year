package Project2_6713221;

/**
 *
 * @author 
 * 6713221 jakkarin roemtangsakul
 * 6713117 Nuttha Limkhunthammo
 * 6713115 Kornchanok Phutrakul
 */
import java.util.*;
import java.lang.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class main extends readdata {

    protected ArrayList<Freight> allf = new ArrayList<>();
    protected ArrayList<Warehouse> allware = new ArrayList<>();
    protected ArrayList<supplier_thread> allsup = new ArrayList<>();
    protected ArrayList<FactoryThread> allfac = new ArrayList<>();
    private Config config;

    static public void main(String argv[]) {
        main mainapp = new main();
        mainapp.working();
    }

    public void working() {
        Config config_local = Config.readConfig();
        this.config = config_local;

        int numSuppliers = this.config.getsupplierNum();
        int numFactories = this.config.getfactoryNum();

        CyclicBarrier afterDayPrint = new CyclicBarrier(numSuppliers + numFactories + 1);
        CyclicBarrier afterSupplierPut = new CyclicBarrier(numSuppliers + numFactories + 1);
        Runnable printBlankAction = () -> {
            System.out.printf("%17s >> \n", Thread.currentThread().getName());
        };

        CyclicBarrier afterFactoryGet = new CyclicBarrier(numFactories + 1, printBlankAction);
        CyclicBarrier afterFactoryCountShip = new CyclicBarrier(numFactories + 1);
        CyclicBarrier afterFactoryShip = new CyclicBarrier(numFactories + 1);
        CyclicBarrier afterFactoryCountLeft = new CyclicBarrier(numFactories + 1);

        String name = Thread.currentThread().getName();
        for (int i = 0; i < this.config.getwarehouseNum(); i++) {
            Warehouse thing = new Warehouse();
            thing.setname("Warehouse_" + i);
            allware.add(thing);
        }
        for (int i = 0; i < config.getfreightNum(); i++) {
            Freight f = new Freight(config.getfreightMaxCapacity());
            f.set_name("Frieght_" + i);
            allf.add(f);
        }
        for (int i = 0; i < config.getsupplierNum(); i++) {
            supplier_thread sup = new supplier_thread("SupplierThread_" + i, afterSupplierPut);
            sup.setmax(config.getsupplierMax());
            sup.setmin(config.getsupplierMin());
            sup.set_barrier(afterDayPrint);
            sup.set_ware(allware);
            allsup.add(sup);
        }

        for (int i = 0; i < config.getfactoryNum(); i++) {
            FactoryThread fac = new FactoryThread("FactoryThread_" + i, 
                    allware, allf, afterDayPrint, afterSupplierPut, afterFactoryGet, afterFactoryCountShip, 
                    afterFactoryShip, afterFactoryCountLeft, config.getfactoryMax(), config.getdays());
            allfac.add(fac);
        }

        printsetup(config);
        for (supplier_thread t : allsup) {
            t.start();
        }
        for (FactoryThread t : allfac) {
            t.start();
        }

        for (int i = 1; i <= this.config.getdays(); i++) {
            printeachday(i, name, afterDayPrint, allf, allware);
            try {
                afterSupplierPut.await();
                afterFactoryGet.await();
                afterFactoryCountShip.await();
                afterFactoryShip.await();
                afterFactoryCountLeft.await();

            } catch (BrokenBarrierException | InterruptedException e) {
            }
        }

        try {
            afterFactoryCountLeft.reset();
            afterFactoryShip.reset();
            afterFactoryCountShip.reset();
            afterFactoryGet.reset();
            afterSupplierPut.reset();
        } catch (Exception e) {
        }

        for (supplier_thread t : allsup) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        for (FactoryThread t : allfac) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        System.out.printf("%17s >> \n", name);

        printSummary();

    }

    public void printsetup(Config con) {
        String name = Thread.currentThread().getName();
        System.out.printf("%17s >>  ================ Parameters ===============\n", name);
        System.out.printf("%17s >> Day of simulation  : %d\n", name, con.getdays());
        System.out.printf("%17s >> Warehouses%9s: [", name, " ");
        for (int i = 0; i < allware.size(); i++) {
            if (i == allware.size() - 1) {
                System.out.printf("%s]\n", allware.get(i).getname());
            } else {
                System.out.printf("%s, ", allware.get(i).getname());
            }
        }
        System.out.printf("%17s >> Freights%11s: [", name, " ");
        for (int i = 0; i < allf.size(); i++) {
            if (i == allf.size() - 1) {
                System.out.printf("%s]\n", allf.get(i).get_name());
            } else {
                System.out.printf("%s, ", allf.get(i).get_name());
            }
        }

        System.out.printf("%17s >> Freights capacity%2s: max = %d\n", name, " ", con.getfreightMaxCapacity());
        System.out.printf("%17s >> SupplierThreads%4s: [", name, " ");
        for (int i = 0; i < allsup.size(); i++) {
            if (i == allsup.size() - 1) {
                System.out.printf("%s]\n", allsup.get(i).getsupname());
            } else {
                System.out.printf("%s, ", allsup.get(i).getsupname());
            }
        }
        System.out.printf("%17s >> Daily supply%7s: min = %d max = %d\n", name, " ", con.getsupplierMin(), con.getsupplierMax());

        System.out.printf("%17s >> FactoryThreads%5s: [", name, " ");
        for (int i = 0; i < allfac.size(); i++) {
            if (i == allfac.size() - 1) {
                System.out.printf("%s]\n", allfac.get(i).getName());
            } else {
                System.out.printf("%s, ", allfac.get(i).getName());
            }
        }
        System.out.printf("%17s >> Daily production%3s: max = %d\n", name, " ", con.getfactoryMax());
    }

    public void printeachday(int d, String n, CyclicBarrier b, ArrayList<Freight> allfreights, ArrayList<Warehouse> allware) {
        for (Freight f : allfreights) {
            f.reset();
        }
        System.out.printf("%17s >> \n", n);
        System.out.printf("%17s >> ===========================================\n", n);
        System.out.printf("%17s >> Day %d\n", n, d);

        for (int i = 0; i < allware.size(); i++) {
            System.out.printf("%17s >> Warehouse_%d balance  = %4d\n", n, i, allware.get(i).getBalance());
        }

        for (int i = 0; i < allf.size(); i++) {
            System.out.printf("%17s >> Freight_%d   capacity = %4d\n", n, i, allfreights.get(i).getCurrentCapacity());
        }

        System.out.printf("%17s >> \n", n);

        for (supplier_thread t : allsup) {
            t.awake();
            if (d == this.config.getdays()) {
                t.setbroke();
            }
        }

        try {
            b.await();
        } catch (BrokenBarrierException | InterruptedException e) {
        }
    }

    public void printSummary() {
        String n = Thread.currentThread().getName();
        System.out.printf("%17s >> ===========================================\n", n);
        System.out.printf("%17s >> Summary\n", n);

        allfac.sort((f1, f2) -> {
            int cmp = Integer.compare(f2.getTotalCreated(), f1.getTotalCreated());
            if (cmp == 0) {
                return f1.getName().compareTo(f2.getName());
            }
            return cmp;
        });

        for (FactoryThread factory : allfac) {
            System.out.printf("%17s >> %s total products = %d shipped = %d (%.2f%%)\n", 
                    n, factory.getName(), factory.getTotalCreated(), factory.getTotalShipped(), factory.getShippingPercentage());
        }
    }
}
