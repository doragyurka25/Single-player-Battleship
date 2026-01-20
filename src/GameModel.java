import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class GameModel {
    final int gridSize = 10;
    private final ArrayList<Ship> ships = new ArrayList<Ship>();
    private int[][] shipStatus;
    private boolean[][] shots;
    private int score = 0;


    public void resetGame() {
        shipStatus = new int[gridSize][gridSize];
        shots = new boolean[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                shipStatus[i][j] = 0;
                shots[i][j] = false;
            }
        }
        placeShips();
    }

    public GameModel() {
        ships.add(new Ship(5, 1));
        ships.add(new Ship(4, 2));
        ships.add(new Ship(3, 3));
        ships.add(new Ship(3, 4));
        ships.add(new Ship(2, 5));
        resetGame();
    }

    public boolean canPlace(int coordx, int coordy, int size, boolean irany) {
        if (irany) {
            if (coordy + size > gridSize) {
                return false;
            }
            for (int j = 0; j < size; j++) {
                if (shipStatus[coordx][coordy + j] != 0) {
                    return false;
                }
            }
        } else {
            if (coordx + size > gridSize) {
                return false;
            }
            for (int j = 0; j < size; j++) {
                if (shipStatus[coordx + j][coordy] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeShips() {
        Random r = new Random();
        for (Ship i : ships) {
            int coordx = r.nextInt(gridSize);
            int coordy = r.nextInt(gridSize);
            boolean vizszint = r.nextBoolean();

            while (!canPlace(coordx, coordy, i.size, vizszint)) {
                coordx = r.nextInt(gridSize);
                coordy = r.nextInt(gridSize);
                vizszint = r.nextBoolean();
            }

            if (vizszint) {
                i.setPosx(coordx);
                i.setPosy(coordy);
                i.setAlignment(true);
                for (int j = 0; j < i.size; j++) {
                    shipStatus[coordx][coordy + j] = i.shipID;
                }
            } else {
                i.setPosx(coordx);
                i.setPosy(coordy);
                i.setAlignment(false);
                for (int j = 0; j < i.size; j++) {
                    shipStatus[coordx + j][coordy] = i.shipID;
                }
            }
        }
    }


    public int shoot(int shotX, int shotY) {
        if (shots[shotX][shotY]) {
            return -1;
        }
        shots[shotX][shotY] = true;
        return shipStatus[shotX][shotY];
    }

    public boolean isDestroyed(Ship ship) {
        int x = ship.getPosx();
        int y = ship.getPosy();
        int size = ship.getSize();
        boolean destroyed = true;
        if (ship.getAlignment()) {
            for (int i = 0; i < size; i++) {
                if (!shots[x][y + i]) {
                    destroyed = false;
                    break;
                }
            }
            if (destroyed) {
                score += ship.getShipID();
                return true;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (!shots[x + i][y]) {
                    destroyed = false;
                    break;
                }
            }
            if (destroyed) {
                score += ship.getShipID();
                return true;
            }
        }
        return false;
    }

    public long countSunkShips() {
        return ships.stream()
                .filter(this::isDestroyed)
                .count();
    }

    public Ship getShipByCoord(int x, int y) {
        for (Ship i : ships) {
            int startX = i.getPosx();
            int startY = i.getPosy();

            if (i.getAlignment()) {
                for (int j = 0; j < i.size; j++) {
                    if (startX == x && startY + j == y) {
                        return i;
                    }
                }
            } else {
                for (int j = 0; j < i.size; j++) {
                    if (startX + j == x && startY == y) {
                        return i;
                    }
                }
            }
        }
        return null;
    }

    public boolean allShipsSunk() {
        for (Ship s : ships) {
            if (!isDestroyed((s))) {
                return false;
            }
        }
        return true;

    }

    public int getScore() {
        return this.score;
    }

}
