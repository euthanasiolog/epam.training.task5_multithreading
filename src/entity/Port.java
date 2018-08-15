package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piatr on 15.08.18.
 */
public class Port {
    private static List<Dock> dockList = new ArrayList<>();

    public List<Dock> getDockList() {
        return dockList;
    }

    public void addDock(Dock dock) {
        dockList.add(dock);
    }

}
