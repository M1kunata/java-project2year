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
    private CyclicBarrier barrier;
    private boolean check = false;
    private boolean broke = false;
    public supplier_thread(String n) {
        super(n);
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
    @Override
    public void run() {
        while (true) {
            sleep();
            Random ran = new Random();
            int select = ran.nextInt(0, 3);
            
            try {
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
            }
            if(broke)break;
        }
    }
}
