package entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by piatr on 15.08.18.
 */
public class Ship {
    private volatile int cargo;
    private Deque<String> item;

    public Ship(int cargo) {
        this.cargo = cargo;
        item = new ArrayDeque<>(cargo);
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public Deque<String> getItem() {
        return item;
    }

}
