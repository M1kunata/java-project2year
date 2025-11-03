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
public class supplier_thread extends Thread {

    private int min, max;
    private String threadname;
    private ArrayList<Warehouse> allw;
    private Random ran = new Random();
    private CyclicBarrier barrier;
    private CyclicBarrier afterSupplierPut;
    private boolean check = false;
    private boolean broke = false;

    public supplier_thread(String n) {
        super(n);
        threadname = n;
    }
    public void setaffersup(CyclicBarrier c){this.afterSupplierPut=c;}
    public String getsupname() {
        return threadname;
    }

    public void setmin(int m) {
        min = m;
    }

    public void setmax(int m) {
        max = m;
    }

    public void set_barrier(CyclicBarrier b) {
        barrier = b;
    }

    public void set_ware(ArrayList<Warehouse> w) {
        allw = w;
    }

    public synchronized void sleep() {
        while (!check) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        check = false;
    }

    public synchronized void awake() {
        check = true;
        notifyAll();
    }

    public void setbroke() {
        broke = true;
    }

    private void put() {
        int select = ran.nextInt(0, allw.size());
        int mete = ran.nextInt(min, max + 1);
        int newBalance = allw.get(select).put(mete);
        System.out.printf("%17s >> put %2d materials %6s %s balance = %3d\n", 
                Thread.currentThread().getName(), mete, " ", allw.get(select).getname(), newBalance);
    }

    @Override
    public void run() {
        while (true) {
            sleep();

            put();
            try {
                barrier.await();

                afterSupplierPut.await();

            } catch (BrokenBarrierException | InterruptedException e) {
                if (broke) {
                    break;
                }
            }

            if (broke) {
                break;
            }

        }
    }
}