package threadconcurrency;

import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadConcurrency {
    
    private final TreeMap<Integer, Integer> map = new TreeMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        final ThreadConcurrency tc = new ThreadConcurrency();
    }
    
    public ThreadConcurrency() {
        
        final long start = System.currentTimeMillis();
        
        final Thread t1 = new Thread(new T1());
        final Thread t2 = new Thread(new T2());
        
        t1.start();
        t2.start();
        
        while (t1.isAlive() || t2.isAlive()) {
            
        }
        
        System.out.println("Finished in: " + (System.currentTimeMillis() - start) + "MS");
        System.out.println("Map filled with: " + map.size() + " int:s");// 200, IF NO CONCURRENCY
    }
    
    
    //  JUST AS FAST AS A DIRECT REFERENCE, SAME ACCURACY
    private TreeMap<Integer, Integer> getTm() {
        lock.lock();
        try {
            return map;
        } finally {
            lock.unlock();
        }
    }
    
    // JUST AS FAST AS A DIRECT REFERENCE, 100% ACCURACY
    private void putSync(final int i) {
        lock.lock();
        try {
            map.put(i, 1);
        } finally {
            lock.unlock();
        }
    }
    
    // THREAD ONE
    class T1 implements Runnable {

        @Override
        public void run() {
            
            for (int i = 0; i < 100; i++) {
                //map.put(i, 1);
                //getTm().put(i, 1);
                putSync(i);
                
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadConcurrency.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    // THREAD TWO
    class T2 implements Runnable {

        @Override
        public void run() {
            
            for (int i = 200; i < 300; i++) {
                //map.put(i, 1);
                //getTm().put(i, 1);
                putSync(i);
                
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadConcurrency.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
}
