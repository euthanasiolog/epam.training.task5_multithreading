package entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 15.08.18.
 */
public class Port {
    private int dock; // количество доков
    private int capacity; // вместимость склада
    private Deque<String> storage; // склад
    private static Port instance;
    private static ReentrantLock lock = new ReentrantLock();

    private Port(int dock, int capacity) {
        this.dock = dock;
        this.capacity = capacity;
        storage = new ArrayDeque<>(capacity);
    }

    public static Port getInstance(int dock, int capacity) {
        if (instance == null) {
            lock.lock();
            try
            {
                if (instance == null) return new Port(dock, capacity);
            } finally {
            lock.unlock();
            }
        }
        return instance;
    }

    public int getDock() {
        return dock;
    }

    public int getCapacity() {
        return capacity;
    }

    public Deque<String> getStorage() {
        return storage;
    }

    public boolean addItem (String item) {
        return storage.offerLast(item);      // Проверить, работает ли это
    }

    public String removeItem() {
        return storage.pollFirst();
    }
}
