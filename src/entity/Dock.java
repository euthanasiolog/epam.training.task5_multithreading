package entity;

/**
 * Created by piatr on 15.08.18.
 */
public class Dock {
    private int capacity;
    private String name;

    public Dock(int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
