package Project2_6713221;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author
 * 6713221 jakkarin roemtangsakul
 * 6713117 Nuttha Limkhunthammo
 * 6713115 Kornchanok Phutrakul
 */
public class FactoryThread extends Thread {
    private int unshippedProducts = 0;
    private int totalCreated = 0;
    private int totalShipped = 0;

    private  List<Warehouse> warehouses;
    private  List<Freight> freights;
    
    private  CyclicBarrier afterDayPrint;
    private  CyclicBarrier afterSupplierPut;
    private  CyclicBarrier afterFactoryGet;
    private  CyclicBarrier afterFactoryCountShip;
    private  CyclicBarrier afterFactoryShip;
    private  CyclicBarrier afterFactoryCountLeft;
    
    private  int maxProduction;
    private  int days;
    private final Random random;

    public FactoryThread(String name) {
        super(name);
        this.random = new Random();
    }
    public void setware(List<Warehouse> w){this.warehouses=w;}
    public void setfrieght(List<Freight> f){this.freights=f;}
    public void setafterday(CyclicBarrier c){this.afterDayPrint=c;}
    public void setaftersupplier(CyclicBarrier c){this.afterSupplierPut=c;}
    public void setafterfacget(CyclicBarrier c){this.afterFactoryGet=c;}
    public void setafterfaccount(CyclicBarrier c){this.afterFactoryCountShip=c;}
    public void setafterfacship(CyclicBarrier c){this.afterFactoryShip=c;}
    public void setafterfaccountlef(CyclicBarrier c){this.afterFactoryCountLeft=c;}
    public void setmaxPro(int d){this.maxProduction=d;}
    public void setdays(int d){this.days=d;}
    public int getTotalCreated() { return totalCreated; }
    public int getTotalShipped() { return totalShipped; }
    
    public double getShippingPercentage() {
        if (totalCreated == 0) return 0.0;
        return (double) totalShipped / totalCreated * 100.0;
    }

    @Override
    public void run() {
        try {
            for (int day = 1; day <= days; day++) {
                afterDayPrint.await();
                afterSupplierPut.await();
                int whIndex = random.nextInt(warehouses.size());
                int producedToday = warehouses.get(whIndex).get(maxProduction); 
                totalCreated += producedToday;
                
                System.out.printf("%17s >> get %2d materials        Warehouse_%d balance = %3d\n", 
                        Thread.currentThread().getName(), producedToday, whIndex, warehouses.get(whIndex).getBalance());
                
                afterFactoryGet.await();
                
                int toShip = producedToday + unshippedProducts;
                
                System.out.printf("%17s >> total products to ship = %4d\n",
                    Thread.currentThread().getName(), toShip);
                
                afterFactoryCountShip.await();
                
                int freightIndex = random.nextInt(freights.size());
                int shipped = freights.get(freightIndex).ship(toShip); 
                totalShipped += shipped;
                
                System.out.printf("%17s >> ship %4d products Freight_%d remaining capacity = %4d\n", 
                        Thread.currentThread().getName(), shipped, freightIndex, freights.get(freightIndex).getCurrentCapacity());
                
                afterFactoryShip.await();
                
                unshippedProducts = toShip - shipped;
                
                System.out.printf("%17s >> unshipped products = %4d\n", Thread.currentThread().getName(), unshippedProducts);
                
                afterFactoryCountLeft.await();
            }
        } catch (BrokenBarrierException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                 Thread.currentThread().interrupt();
            }
        }
    }
}
    

