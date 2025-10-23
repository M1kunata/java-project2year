package Project2_6713221;

/**
 *
 * @author jakka
 */
import java.util.*;
import java.lang.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
/*class Frieght //wait for gamyui
{
    public String name;
    public int capacity ;
}*/
/*class warehouse//wait for gamyui
{
    public String name;
    public int balance=0;
    
}*/
public class main extends gamyui //extend otherfile function
{
     //wait for data from readfile of gamyui where class collect file
        int day=5;
        int mindaisup=50;
        int maxdaisup=100;
        int maxfriecap=100;
        int maxdaipro = 100;
        int numofsupth =3;
         //wait for data from readfile of gamyui
     protected ArrayList<Freight> allf =new ArrayList<>();
     protected ArrayList<Warehouse> allware = new ArrayList<>();
     protected ArrayList<supplier_thread> allsup = new ArrayList<>();
      static public void main(String argv[]){
        main mainapp = new main();
        mainapp.working();
    }
    public void working() 
    {
        //readfile
        Config config = Config.readConfig("src/main/Java/Project2_6713221/config_1.txt");
        CyclicBarrier barrier = new CyclicBarrier(4);//wait to change to be sum of supplier + factor thread
        String name = Thread.currentThread().getName();
        for(int i=0;i<config.getwarehouseNum();i++)
        {
            Warehouse thing = new Warehouse();
            thing.setname("warehouse_"+i);//function set name of warehouse
            allware.add(thing);
        }
        for(int i=0;i<config.getfreightNum();i++)
        {
            Freight f = new Freight(config.getfreightMaxCapacity());
            f.set_name("Frieght_"+i);
            allf.add(f);
        }
        for(int i=0;i<config.getsupplierNum();i++)
        {
            supplier_thread sup = new supplier_thread("supplierThreads_"+i);
            sup.setmax(config.getsupplierMax());
            sup.setmin(config.getsupplierMin());
            sup.set_barrier(barrier);
            sup.set_ware(allware);
            allsup.add(sup);
        }
        printsetup(config);
        for(supplier_thread t : allsup)
        {t.start();}
        for(int i=1;i<=day;i++)
        {
          printeachday(i,name,barrier);
        }
        
        
        
    }        
    public void printsetup(Config con)
    {        
        String name = Thread.currentThread().getName();
        //print set up
        System.out.println(name+" >>  ================ Parameters ===============");
        System.out.printf("%s >> Day of simulation  : %d\n",name,con.getdays() );
        System.out.printf("%s >> Warehouses%9s: [",name," ");
        for(int i=0;i<allware.size();i++)
        {
            if(i==allware.size()-1)
                System.out.printf("%s]\n",allware.get(i).getname());
            else             System.out.printf("%s, ",allware.get(i).getname());
        }
        System.out.printf("%s >> Frieghts%11s: [",name," ");
        for(int i=0;i<allf.size();i++)
        {
            if(i==allf.size()-1)
                System.out.printf("%s]\n",allf.get(i).get_name());
            else    System.out.printf("%s, ",allf.get(i).get_name());
        }

        System.out.printf("%s >> Frieghts capacity%2s: max = %d\n",name," ",con.getfreightMaxCapacity());
        System.out.printf("%s >> SupplierThreads%4s: [",name," ");
        for(int i =0;i<allsup.size();i++)
        {
            if(i==allsup.size()-1)
                System.out.printf("%s]\n",allsup.get(i).getsupname());
            else System.out.printf("%s, ",allsup.get(i).getsupname());
        }
        System.out.printf("%s >> Daily supply%7s: min = %d max = %d\n",name," ",con.getsupplierMin(),con.getsupplierMax());
        System.out.printf("%s >> FactoryThreads%5s: \n",name," ");
        //wait to yok
        System.out.printf("%s >> Daily production%3s: max = %d\n",name," ",con.getfactoryMax());
        System.out.printf("%s >> \n",name);
    }
    public void printeachday(int d,String n,CyclicBarrier b)
    {
        System.out.println(n+" >> ===========================================");
        System.out.printf("%s >> Day %d\n",n,d);
        for(int i=0;i<allware.size();i++)
            System.out.printf("%s >> Warehouse_%d balance  = %d\n",n,i,allware.get(i).getBalance());
        for(int i=0;i<allf.size();i++)
            System.out.printf("%s >> Freight_%d   capacity = %d\n",n,i,allf.get(i).getMaxCapacity());
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
