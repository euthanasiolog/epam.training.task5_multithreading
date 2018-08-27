package entity;

import logic.PortManager;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        if (!cargo.equals(ship.cargo)) return false;
        if (!isFull.equals(ship.isFull)) return false;
        if (!dockNumber.equals(ship.dockNumber)) return false;
        return portManager != null ? portManager.equals(ship.portManager) : ship.portManager == null;
    }

    @Override
    public int hashCode() {
        int result = cargo.hashCode();
        result = 31 * result + isFull.hashCode();
        result = 31 * result + dockNumber.hashCode();
        result = 31 * result + (portManager != null ? portManager.hashCode() : 0);
        return result;
    }
}
