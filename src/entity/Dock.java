package entity;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by piatr on 25.08.18.
 */
public class Dock {
    private AtomicBoolean isBusy = new AtomicBoolean();

    public AtomicBoolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy.set(busy);
    }
}
