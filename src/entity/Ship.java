package entity;

import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 15.08.18.
 */
public class Ship implements Callable<Integer>{
    private AtomicInteger cargo = new AtomicInteger();
    private AtomicBoolean isFull = new AtomicBoolean();
    private Semaphore semaphore;
    private ReentrantLock lock = new ReentrantLock();
    private Port port;
    private final static Logger LOGGER = Logger.getLogger(Ship.class);

    public Ship(int cargo, boolean isFull, Semaphore semaphore, Port port) {
        this.cargo.set(cargo);
        this.semaphore = semaphore;
        this.port = port;
        this.isFull.set(isFull);
    }

    @Override
    public Integer call() throws Exception {
        int i = cargo.get();

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            LOGGER.error("acquire failed");
            Thread.currentThread().interrupt();
        }
        lock.lock();
        int j=0;
//        do {
//            if (!port.getDocks()[j].isBusy().get()) {
//                port.getDocks()[j].setBusy(true);
//            } else j++;
//        } while (port.getDocks()[j].isBusy().get());
        for (int k = 0; k<port.getDocks().length;k++){
            if (!port.getDocks()[k].isBusy().get()) {
                port.getDocks()[k].setBusy(true);
                j = k;
                break;
            }
        }
        LOGGER.info("Ship "+Thread.currentThread().getName()+" get dock"+j);
        lock.unlock();
        TimeUnit.SECONDS.sleep(2);
        lock.lock();
        if (isFull.get()) {
            if ((Port.getStorageCapacity()-port.getStorage().get())>=cargo.get()) {
                port.setStorage(port.getStorage().get()+cargo.get());
                cargo.set(0);
            } else {
                //ждать, пока освободится место на складе
                LOGGER.info("waiting 1");
            }
        } else {
            if (port.getStorage().get()>=cargo.get()) {
                port.setStorage(port.getStorage().get()-cargo.get());
                cargo.set(0);
            } else {
                LOGGER.info("waiting2");
                //ждать, пока появится товар на складе
            }
        }
        LOGGER.info("Ship "+Thread.currentThread().getName()+" leave dock"+j);
        port.getDocks()[j].setBusy(false);
        lock.unlock();
        semaphore.release();
        return i;
    }

}
