package entity;

/**
 * Created by piatr on 15.08.18.
 */
public class Ship {
    private int cargo;
    private String[] product;

    public Ship(int cargo) {
        this.cargo = cargo;
        product = new String[cargo];
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public String[] getProduct() {
        return product;
    }

    public void setProduct(String[] product) {
        this.product = product;
    }
}
