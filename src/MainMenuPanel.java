import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private final int scale = 2;
    private BufferedImage img;

    public MainMenuPanel(GameFrame gf) {

        try {
            img = ImageIO.read(new File("img/battleship.png"));
        } catch (IOException e) {
            System.out.println("nem lehetett beolvasni a kepet");
        }
        this.setPreferredSize(new Dimension(600 * scale, 500 * scale));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 50, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.weightx = 1.0;

        //indul a jatek
        JButton startGame = new JButton("Start");

        gbc.gridy = 0;
        startGame.setPreferredSize(new Dimension(150, 50));
        gbc.gridwidth = 3;
        styleButton(startGame);
        add(startGame, gbc);

        //scoreboard
        JButton highScore = new JButton("High Score");

        gbc.gridy = 2;
        highScore.setPreferredSize(new Dimension(250, 50));
        styleButton(highScore);
        add(highScore, gbc);

        //kilepes
        JButton exitGame = new JButton("Exit");
        gbc.gridy = 3;
        exitGame.setPreferredSize(new Dimension(150, 50));
        styleButton(exitGame);
        add(exitGame, gbc);

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gf.showPanel("Game");
            }
        });

        highScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gf.showPanel("High Score");
            }
        });

        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }

    //gombok stilizalasa
    //attetszoseg, background, szoveg szine, korvonalak, betutipus
    public void styleButton(JButton b) {
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setForeground(Color.white);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 40));
    }

    //kell override-olni a paintComponent-et hogy ki tudjam rajzolni a hatterkepet
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, 600 * scale, 500 * scale, null);
    }
}
