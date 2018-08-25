package logic;

import entity.Port;
import entity.Ship;
import org.apache.log4j.Logger;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 25.08.18.
 */
public class PortManager {
    private Semaphore semaphore;
    private ReentrantLock lock = new ReentrantLock();
    private Port port;
    private static final Logger LOGGER = Logger.getLogger(PortManager.class);

    public PortManager(Semaphore semaphore, Port port){
        this.semaphore = semaphore;
        this.port = port;
    }

    public void takeShip(Ship ship) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            LOGGER.error("acquire failed");
            Thread.currentThread().interrupt();
        }
        lock.lock();

//        do {
//            if (!port.getDocks()[j].isBusy().get()) {
//                port.getDocks()[j].setBusy(true);
//            } else j++;
//        } while (port.getDocks()[j].isBusy().get());
        for (int k = 0; k<port.getDocks().length;k++){
            if (!port.getDocks()[k].isBusy().get()) {
                port.getDocks()[k].setBusy(true);
                ship.setDockNumber(k);
                break;
            }
        }
        LOGGER.info("Ship "+Thread.currentThread().getName()+" get dock"+ship.getDockNumber());
        lock.unlock();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("taking ship failed");
        }
    }

    public void leaveShip(Ship ship) {
        lock.lock();
        if (ship.getIsFull().get()) {
            if ((Port.getStorageCapacity()-port.getStorage().get())>=ship.getCargo().get()) {
                port.setStorage(port.getStorage().get()+ship.getCargo().get());
                ship.getCargo().set(0);
            } else {
                //ждать, пока освободится место на складе
                LOGGER.info("waiting 1");
            }
        } else {
            if (port.getStorage().get()>=ship.getCargo().get()) {
                port.setStorage(port.getStorage().get()-ship.getCargo().get());
                ship.getCargo().set(0);
            } else {
                LOGGER.info("waiting2");
                //ждать, пока появится товар на складе
            }
        }
        LOGGER.info("Ship "+Thread.currentThread().getName()+" leave dock"+ship.getDockNumber());
        port.getDocks()[ship.getDockNumber().get()].setBusy(false);
        lock.unlock();
        semaphore.release();
    }


}
