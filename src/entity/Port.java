package entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 15.08.18.
 */
public class Port {
    private static Port instance;
    private Dock[] docks;
    private static final int DOCK_COUNT = 5;
    private AtomicInteger storage;
    private static final int STORAGE_CAPACITY = 300000;
    private static ReentrantLock lock = new ReentrantLock();

    private Port() {
        this.docks = new Dock[DOCK_COUNT];
        for (int i = 0;i<docks.length;i++){
            docks[i]= new Dock();
            docks[i].setBusy(false);
        }
        this.storage = new AtomicInteger();
        storage.set(0);
    }

    public static Port getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null)
                    instance = new Port();
                    return instance;
            }
            finally {
            lock.unlock();
            }
        }
        return instance;
    }

    public Dock[] getDocks() {
        return docks;
    }

    public AtomicInteger getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage.set(storage);
    }

    public static int getStorageCapacity() {
        return STORAGE_CAPACITY;
    }

    public static int getDockCount() {
        return DOCK_COUNT;
    }
}
