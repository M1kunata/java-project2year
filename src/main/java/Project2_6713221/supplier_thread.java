/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project2_6713221;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author jakka
 */
public class supplier_thread extends Thread {

    private int min, max;
    private String threadname;
    private ArrayList<Warehouse> allw;
    private Random ran = new Random();
    private CyclicBarrier barrier;
    private boolean check = false;
    private boolean broke = false;
    public supplier_thread(String n) {
        super(n);
        threadname =n;
    }
    public String getsupname()
    {return threadname;}
    public void setmin(int m) {
        min = m;
    }

    public void setmax(int m) {
        max = m;
    }

    public void set_barrier(CyclicBarrier b) {
        barrier = b;
    }
    public void set_ware(ArrayList<Warehouse> w)
    {
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
    public void setbroke()
    {
        broke=true;
    }
    private synchronized void put()
    {
        int select = ran.nextInt(0, allw.size()); 
            int mete = ran.nextInt(min,max);
            allw.get(select).put(mete);
            System.out.printf("%s >> put %2d materials%6s%s balance = %d\n",Thread.currentThread().getName(),mete," ",allw.get(select).getname(),allw.get(select).getBalance());
    }
    @Override
    public void run() {
        while (true) {
            sleep();
            put();
            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
            }
            if(broke)break;
        }
    }
}
