package logic;

import entity.Port;
import entity.Ship;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by piatr on 25.08.18.
 */
public class StorageManager {
    private Semaphore semaphore;
    private ReentrantLock lock;
    private Ship ship;
    private Port port;

}
