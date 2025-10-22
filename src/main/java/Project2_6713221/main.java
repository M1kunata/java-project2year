package Project2_6713221;

/**
 *
 * @author jakka
 */
import java.util.*;
import java.lang.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class Frieght //wait for gamyui
{
    public String name;
    public int capacity ;
}
class warehouse//wait for gamyui
{
    public String name;
    public int balance=0;
    
}
public class main //extend otherfile function
{
     //wait for data from readfile of gamyui where class collect file
        int day=5;
        int mindaisup=50;
        int maxdaisup=100;
        int maxfriecap=100;
        int maxdaipro = 100;
        int numofsupth =3;
         //wait for data from readfile of gamyui
     protected ArrayList<Frieght> allf =new ArrayList<>();
     protected ArrayList<warehouse> allware = new ArrayList<>();
     protected ArrayList<supplier_thread> allsup = new ArrayList<>();
      static public void main(String argv[]){
        main mainapp = new main();
        mainapp.working();
    }
    public void working() 
    {
        //readfile
        CyclicBarrier barrier = new CyclicBarrier(4);//wait to change to be sum of supplier + factor thread
        String name = Thread.currentThread().getName();
        for(int i=0;i<3;i++)
        {
            warehouse thing = new warehouse();
            thing.name = "warehouse_"+i;//function set name of warehouse
            allware.add(thing);
        }
        for(int i=0;i<2;i++)
        {
            Frieght f = new Frieght();
            f.name="Frieght_"+i;
            f.capacity=maxfriecap;
            allf.add(f);
        }
        for(int i=0;i<numofsupth;i++)
        {
            supplier_thread sup = new supplier_thread("supplierThreads"+i);
            sup.setmax(maxdaisup);
            sup.setmin(mindaisup);
            sup.set_barrier(barrier);
            allsup.add(sup);
            sup.start();
        }
        printsetup();
        
        for(int i=1;i<=day;i++)
        {
          printeachday(i,name,barrier);
        }
        
        
        
    }        
    public void printsetup()
    {        
        String name = Thread.currentThread().getName();
        //print set up
        System.out.println(name+" >>  ================ Parameters ===============");
        System.out.printf("%s >> Day of simulation  : %d\n",name,day );
        System.out.printf("%s >> Warehouses%9s: \n",name," ");
        System.out.printf("%s >> Frieghts%11s: \n",name," ");
        System.out.printf("%s >> Frieghts capacity%2s: max = %d\n",name," ",maxfriecap);
        System.out.printf("%s >> SupplierThreads%4s: \n",name," ");
        System.out.printf("%s >> Daily supply%7s: min = %d max = %d\n",name," ",mindaisup,maxdaisup);
        System.out.printf("%s >> FactoryThreads%5s: \n",name," ");
        System.out.printf("%s >> Daily production%3s: max = %d\n",name," ",maxdaipro);
        System.out.printf("%s >> \n",name);
    }
    public void printeachday(int d,String n,CyclicBarrier b)
    {
        System.out.println(n+" >> ===========================================");
        System.out.printf("%s >> Day %d\n",n,d);
        for(int i=0;i<allware.size();i++)
            System.out.printf("%s >> Warehouse_%d balance  = %d\n",n,i,allware.get(i).balance);
        for(int i=0;i<allf.size();i++)
            System.out.printf("%s >> Freight_%d   capacity = %d\n",n,i,allf.get(i).capacity);
        for(supplier_thread t : allsup)
        {
            t.awake();
            if(d==day) t.setbroke();
        }
        try{
          b.await();
          }
        catch(BrokenBarrierException|InterruptedException e){}
        
    }
}
