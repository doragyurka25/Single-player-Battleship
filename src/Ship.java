import java.awt.*;
import java.awt.image.BufferedImage;

public class Ship {
    final int size;
    final int shipID;
    private int posx;
    private int posy;
    private boolean vizszintes;

    public Ship(int size, int id) {
        this.size = size;
        this.shipID = id;
        this.posx = 0;
        this.posy = 0;
        this.vizszintes = true;
    }

    public int getSize() {

        return size;
    }

    public void setPosx(int x) {
        this.posx = x;
    }

    public void setPosy(int y) {
        this.posy = y;
    }

    public void setAlignment(boolean a) {
        this.vizszintes = a;
    }

    public boolean getAlignment() {
        return this.vizszintes;
    }

    public int getPosx() {
        return this.posx;
    }

    public int getPosy() {
        return this.posy;
    }

    public int getShipID() {
        return this.shipID;
    }

}
