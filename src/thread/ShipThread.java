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
        LOGGER.info("корабль "+ship.getId()+ " приплыл");
        if (ship.getItem().peekFirst()!=null) {
            for (String s : ship.getItem()) {
                if (port.getStorage().size()<port.getCapacity()){
                    port.addItem(s);
                    ship.getItem().remove(s);
                    LOGGER.info("на склад c корабля "+ship.getId()+" разгружено: "+ s);
                }
            }
            LOGGER.info("корабль "+ship.getId()+" разгрузился");
        }
        for (int i = 0; i <ship.getCargo(); i++){
//            if (port.removeItem()!=null) {
              if (true) {
                String s = port.removeItem();
                ship.getItem().push(s);
                LOGGER.info("на корабль "+ship.getId()+" загружено: "+ s);
            }
        }
        LOGGER.info("корабль "+ship.getId()+" загрузился");
        LOGGER.info("корабль "+ship.getId()+" отплыл");
        semaphore.release();
        return 1;
    }
}
