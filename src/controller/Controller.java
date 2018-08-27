package controller;

import entity.Port;
import entity.Ship;
import logic.PortManager;
import logic.StorageManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by piatr on 15.08.18.
 */
public class Controller {
    private static final Port PORT = Port.getInstance();
    private static final Semaphore SEMAPHORE = new Semaphore(Port.getDockCount(), true);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Logger LOGGER = Logger.getLogger(Controller.class);
    private static PortManager portManager = new PortManager(SEMAPHORE, PORT);

    public static void main(String[] args) {
        List<Callable<Integer>> callable = new ArrayList<>();
        for (int i = 0; i<15; i++) {
            callable.add(new Ship((int) (Math.random()*20+2), true, portManager));
        }
        for (int i = 0; i<15; i++) {
            callable.add(new Ship((int) (Math.random()*20+2), false, portManager));
        }
        try {
            StorageManager storageManager = new StorageManager(PORT);
            storageManager.setDaemon(true);
            storageManager.start();
            executorService.invokeAll(callable);
            executorService.shutdown();
        } catch (InterruptedException e) {
            LOGGER.error("error");
        }
    }
}
