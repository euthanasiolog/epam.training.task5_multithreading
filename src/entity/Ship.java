package entity;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by piatr on 15.08.18.
 */
public class Ship {
    private int id;
    private int cargo;
    private Deque<String> item;

    public Ship(int cargo, int id) {
        this.id = id;

        this.cargo = cargo;
        item = new ArrayDeque<>(cargo);
        for (int i = 0; i<cargo; i++) {
            item.add(id+" "+String.valueOf(Math.random()*100+1));
        }
    }

    public int getId() {
        return id;
    }

    public int getCargo() {
        return cargo;
    }

    public Deque<String> getItem() {
        return item;
    }

}
