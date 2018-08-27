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
        int j = 0;
        boolean b = false;
        while (!b&&j<port.getDocks().length){
            if (!port.getDocks()[j].isBusy().get()){
                port.getDocks()[j].setBusy(true);
                ship.setDockNumber(j);
                b = true;
            }
            j++;
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
        if (ship.getCargo().get() > Port.getStorageCapacity() / 2) {// если такой большой корабль, то не будем разгружать,
                                                                    // а то места на складе не хватит или товаров.
            LOGGER.info("Ship " + Thread.currentThread().getName() + " leave dock with nothing " + ship.getDockNumber());
            port.getDocks()[ship.getDockNumber().get()].setBusy(false);
            lock.unlock();
            semaphore.release();
        } else {
            if (ship.getIsFull().get()) {

                if ((Port.getStorageCapacity() - port.getStorage().get()) >= ship.getCargo().get()) {
                    port.setStorage(port.getStorage().get() + ship.getCargo().get());
                    ship.getCargo().set(0);
                    LOGGER.info("Ship " + Thread.currentThread().getName() + " leave dock" + ship.getDockNumber());
                    port.getDocks()[ship.getDockNumber().get()].setBusy(false);
                    lock.unlock();
                    semaphore.release();
                } else {
                    //рекурсивно ждать, пока освободится место на складе
                    LOGGER.info("waiting 1 " + Thread.currentThread().getName());
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.error("waiting 1 error");
                    }
                    lock.unlock();
                    leaveShip(ship);
                }
            } else {
                if (port.getStorage().get() >= ship.getCargo().get()) {
                    port.setStorage(port.getStorage().get() - ship.getCargo().get());
                    ship.getCargo().set(0);
                    LOGGER.info("Ship " + Thread.currentThread().getName() + " leave dock" + ship.getDockNumber());
                    port.getDocks()[ship.getDockNumber().get()].setBusy(false);
                    lock.unlock();
                    semaphore.release();
                } else {
                    LOGGER.info("waiting2");
                    lock.unlock();
                    //рекурсивно ждать, пока появится товар на складе
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.error("waiting 2 error");
                    }
                    leaveShip(ship);
                }
            }
        }
    }
}
