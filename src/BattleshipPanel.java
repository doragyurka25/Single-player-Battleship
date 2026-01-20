import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BattleshipPanel extends JPanel {
    private BufferedImage img, robbanas, wrong, Battleship, Carrier, Destroyer, Patrol, Submarine;
    private BufferedImage RBattleship, RCarrier, RDestroyer, RPatrol, RSubmarine;

    final int gridSize = 10;
    final int player_gridx = 100;
    final int player_gridy = 150;
    final int width = 600;
    final int height = 500;
    private final GameModel board;
    private final ArrayList<Point> missedShots = new ArrayList<Point>();
    private final ArrayList<Point> hitShots = new ArrayList<Point>();
    private final ArrayList<Ship> hitShips = new ArrayList<Ship>();
    private final ArrayList<Ship> destroyedShips = new ArrayList<Ship>();
    private final Stopper stopper;
    //itt lesznek kepernyobeallitasok
    final int originalTileSize = 28;
    final int scale = 2;

    private Point currentExplosion = null;
    private Timer explosionTimer;

    public BattleshipPanel(GameModel gm, GameFrame gf, Stopper stopper) {
        this.board = gm;
        this.stopper = stopper;
        add(stopper);

        setOpaque(false);
        try {
            img = ImageIO.read(new File("img/battleship.png"));

            // a vizszintes kirajzolashoz szukseges hajok
            robbanas = ImageIO.read(new File("img/Robbanas.png"));
            Battleship = ImageIO.read(new File("img/Battleshipp.png"));
            Carrier = ImageIO.read(new File("img/Carrier.png"));
            Destroyer = ImageIO.read(new File("img/Destroyer.png"));
            Patrol = ImageIO.read(new File("img/Patrol.png"));
            Submarine = ImageIO.read(new File("img/Submarine.png"));

            // a fuggoleges kirajzolashoz szukseges hajok
            RBattleship = ImageIO.read(new File("img/RBattleshipp.png"));
            RCarrier = ImageIO.read(new File("img/RCarrier.png"));
            RDestroyer = ImageIO.read(new File("img/RDestroyer.png"));
            RPatrol = ImageIO.read(new File("img/RPatrol.png"));
            RSubmarine = ImageIO.read(new File("img/RSubmarine.png"));

            wrong = ImageIO.read(new File("img/Wrong.png"));

        } catch (IOException e) {
            System.out.println("nem lehetett beolvasni a kepet");
        }

        this.setPreferredSize(new Dimension(width * scale, height * scale));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // lekerem hogy hova kattintottunk az egerrel
                int mouseX = e.getX();
                int mouseY = e.getY();

                // ha a tablan kivul
                if (mouseX < player_gridx || mouseY < player_gridy) {
                    return;
                }

                // atalakitom a tabla meretehez megfeleloen a kattintas koordinatait
                int cellSize = originalTileSize * scale;
                int gridX = (mouseX - player_gridx) / cellSize;
                int gridY = (mouseY - player_gridy) / cellSize;

                // ha nincs benne a logikai tablaban
                if (gridX >= gridSize || gridY >= gridSize) {
                    return;
                }

                // a GameModel osztalybol ellenorzom a shoot metodussal ha ervenyes-e a loves
                int shot = board.shoot(gridY, gridX);
                if (shot == -1) {
                    // ha nem akkor visszaterek
                    return;
                }
                // lekerem a loves koordinatai szerint hogy melyik hajot celoztam meg
                Ship s = board.getShipByCoord(gridY, gridX);
                if (s != null) {
                    if (board.isDestroyed(s) && !destroyedShips.contains(s)) {
                        // eltarolom egy listaba ha a teljes hajot kilottem
                        try {
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("img/destroyed.wav"));
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            clip.start();
                        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException a) {
                            System.out.println("IO error");
                        }
                        destroyedShips.add(s);
                    }
                }

                // lementem egy valtozoba es kirajzolom
                currentExplosion = new Point(gridY, gridX);
                repaint();

                // ha mar fut a timer akkor leallitom
                if (explosionTimer != null && explosionTimer.isRunning()) {
                    explosionTimer.stop();
                }

                // letrehozok minden egyes lovesnel egy uj timer-t aminek az a szerepe, hogy miutan kirajzolodik egy robbanas
                // tuntesse el megadott ido mulva
                try {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("img/robbanas.wav"));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException a) {
                    System.out.println("IO error");
                }
                explosionTimer = new Timer(1400, evt -> {
                    currentExplosion = null;
                    if (shot != 0) {
                        // ha talalt
                        hitShots.add(new Point(gridY, gridX));
                        Ship hitShip = board.getShipByCoord(gridY, gridX);
                        if (hitShip != null) {
                            hitShips.add(hitShip);
                        }
                    } else {
                        // ha nem
                        missedShots.add(new Point(gridY, gridX));
                    }
                    repaint();
                    explosionTimer.stop();
                });
                explosionTimer.setRepeats(false); //uj sor
                explosionTimer.start();

                if (board.countSunkShips() == 5) {
                    int score = board.getScore();
                    stopper.timerStop();
                    gf.showVictory(score);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // kirajzolom a hatteret
        g.drawImage(img, 0, 0, width * scale, height * scale, null);

        //jatekos tablaja
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.black);
        for (int i = 0; i <= gridSize; i++) {
            int y = player_gridy + i * originalTileSize * scale;
            g2.drawLine(player_gridx, y, player_gridx + (gridSize * originalTileSize * scale), y);
        }

        for (int j = 0; j <= gridSize; j++) {
            int x = j * originalTileSize * scale + player_gridx;
            g2.drawLine(x, player_gridy, x, player_gridy + (gridSize * originalTileSize * scale));
        }

        // a nem talalatot rejto cellakba pont
        g2.setColor(Color.blue);
        for (Point p : missedShots) {
            int x = player_gridx + p.y * originalTileSize * scale;
            int y = player_gridy + p.x * originalTileSize * scale;
            g2.fillOval(x + originalTileSize - 7, y + originalTileSize - 5, 10, 10);
        }

        // a talalatot rejto cellakba X-et rakok
        for (int i = 0; i < hitShots.size(); i++) {
            Point p = hitShots.get(i);
            Ship shipID = hitShips.get(i);

            // ellenorzom hogy ez a hajo elsullyedt-e mar
            boolean shipDestroyed = false;
            if (destroyedShips.contains(shipID)) {
                shipDestroyed = true;
            }
            if (!shipDestroyed) {
                g2.drawImage(wrong, player_gridx + p.y * (originalTileSize * scale), player_gridy + p.x * (originalTileSize * scale), (originalTileSize * scale), (originalTileSize * scale), null);
            }
        }

        // ha egy hajot teljes egeszeben kilottem azutan rajzolom csak ki a tablara
        for (Ship i : destroyedShips) {
            int startX = player_gridx + i.getPosy() * originalTileSize * scale;
            int startY = player_gridy + i.getPosx() * originalTileSize * scale;
            BufferedImage shipImg = null;
            int width, height;
            if (i.getAlignment()) {
                width = i.size * originalTileSize * scale;
                height = originalTileSize * scale;
            } else {
                width = originalTileSize * scale;
                height = i.size * originalTileSize * scale;
            }

            switch (i.shipID) {
                case (1):
                    if (i.getAlignment()) {
                        shipImg = Battleship;
                    } else {
                        shipImg = RBattleship;
                    }
                    break;
                case (2):
                    if (i.getAlignment()) {
                        shipImg = Carrier;
                    } else {
                        shipImg = RCarrier;
                    }
                    break;
                case (3):
                    if (i.getAlignment()) {
                        shipImg = Submarine;
                    } else {
                        shipImg = RSubmarine;
                    }
                    break;
                case (4):
                    if (i.getAlignment()) {
                        shipImg = Destroyer;
                    } else {
                        shipImg = RDestroyer;
                    }
                    break;
                case (5):
                    if (i.getAlignment()) {
                        shipImg = Patrol;
                    } else {
                        shipImg = RPatrol;
                    }
                    break;
            }
            g2.drawImage(shipImg, startX, startY, width, height, null);
        }

        //mindig kirajzolom a robbanast
        if (currentExplosion != null) {

            g2.drawImage(robbanas, player_gridx + currentExplosion.y * originalTileSize * scale, player_gridy + currentExplosion.x * originalTileSize * scale, (originalTileSize * scale), (originalTileSize * scale), null);
        }

    }

    public void resetGame() {
        missedShots.clear();
        hitShots.clear();
        destroyedShips.clear();
        hitShips.clear();
        stopper.timerReset();
        currentExplosion = null;
        repaint();
    }


}
