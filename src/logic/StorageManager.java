package logic;

import entity.Port;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 25.08.18.
 */
public class StorageManager extends Thread {
    private ReentrantLock lock = new ReentrantLock();
    private Port port;
    private static final Logger LOGGER = Logger.getLogger(StorageManager.class);

    public StorageManager (Port port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
         lock.lock();
         if (port.getStorage().get()<(Port.getStorageCapacity()/5)) {
             port.setStorage(port.getStorage().get()+(Port.getStorageCapacity()/2));
             //если на складе мало товара, добавляем половину вместимости
         }
         lock.unlock();
         lock.lock();
         if (port.getStorage().get()>(Port.getStorageCapacity()/5)*3) {
             port.setStorage(Port.getStorageCapacity()/5);
             //а если много, то разгружаем склад
         }
         lock.unlock();
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("demon error");
            }
        }
    }
}
