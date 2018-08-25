package logic;

import entity.Port;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 25.08.18.
 */
public class StorageManager extends Thread {
    private ReentrantLock lock = new ReentrantLock();
    private Port port;

    public StorageManager (Port port) {
        this.port = port;

    }

    @Override
    public void run() {
        while (true) {
         lock.lock();
         if (port.getStorage().get()<(Port.getStorageCapacity()/5)) {
             port.setStorage(port.getStorage().get()+(Port.getStorageCapacity()/2));
         }
         lock.unlock();
         lock.lock();
         if (port.getStorage().get()>(Port.getStorageCapacity()/5)*4) {
             port.setStorage(Port.getStorageCapacity()/2);
         }
         lock.unlock();
        }
    }
}
