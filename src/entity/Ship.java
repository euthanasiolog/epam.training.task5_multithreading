package entity;

import logic.PortManager;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by piatr on 15.08.18.
 */
public class Ship implements Callable<Integer>{
    private AtomicInteger cargo = new AtomicInteger();
    private AtomicBoolean isFull = new AtomicBoolean();
    private AtomicInteger dockNumber = new AtomicInteger();
    private final static Logger LOGGER = Logger.getLogger(Ship.class);
    private PortManager portManager;

    public Ship(int cargo, boolean isFull, PortManager portManager) {
        this.cargo.set(cargo);
        this.isFull.set(isFull);
        this.portManager = portManager;
    }

    @Override
    public Integer call() throws Exception {
        int i = cargo.get();
        portManager.takeShip(this);
        portManager.leaveShip(this);
        return i;
    }

    public AtomicInteger getCargo() {
        return cargo;
    }

    public AtomicBoolean getIsFull() {
        return isFull;
    }

    public AtomicInteger getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(int dockNumber) {
        this.dockNumber.set(dockNumber);
    }
}
