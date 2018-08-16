package thread;

import entity.Port;
import entity.Ship;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * Created by piatr on 16.08.18.
 */
public class ShipThread implements Callable<Integer> {
    private final Logger LOGGER = Logger.getLogger(ShipThread.class);
    private Ship ship;
    private Port port;
    private Semaphore semaphore;

    public ShipThread(Ship ship, Port port, Semaphore semaphore) {
        this.ship = ship;
        this.port = port;
        this.semaphore = semaphore;
    }

    @Override
    public Integer call() throws Exception {
        semaphore.acquire();
        LOGGER.info("корабль приплыл");
        if (ship.getItem().peekFirst()!=null) {
            for (String s : ship.getItem()) {
                while (port.addItem(s)){
                    port.addItem(s);
                    ship.getItem().remove(s);
                    LOGGER.info("на склад загружено: "+ s);
                }
                LOGGER.info("корабль разгрузился");
            }
        }
        while (ship.getItem().size()<ship.getCargo()){
            if (port.removeItem()!=null) {
                String s = String.valueOf(port.removeItem());
                ship.getItem().push(s);
                LOGGER.info("на корабль загружено: "+ s);
            }
        }
        LOGGER.info("корабль загрузился");
        LOGGER.info("корабль отплыл");
        semaphore.release();
        return 1;
    }
}
