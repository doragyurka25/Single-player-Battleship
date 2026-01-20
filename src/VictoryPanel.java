import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VictoryPanel extends JPanel {
    private JButton ujra;
    private JButton scores;
    private JButton exit;
    private BufferedImage img;
    private boolean scoreSaved = false;

    public VictoryPanel(GameFrame gf, int score) {
        this.setOpaque(false);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(80, 110, 80, 150);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.weightx = 1.0;


        gbc.gridy = 1;
        JLabel label = new JLabel("VICTORY");
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setPreferredSize(new Dimension(250, 50));
        label.setForeground(Color.white);
        gbc.insets = new Insets(30, 120, 80, 110);
        add(label, gbc);


        JButton saveScore = new JButton("Save Score");
        gbc.gridy = 2;
        saveScore.setPreferredSize(new Dimension(250, 50));
        gbc.gridwidth = 3;
        styleButton(saveScore);
        add(saveScore, gbc);

        //ujra
        JButton restartGame = new JButton("New Game");

        gbc.gridy = 3;
        restartGame.setPreferredSize(new Dimension(250, 50));
        gbc.gridwidth = 3;
        styleButton(restartGame);
        add(restartGame, gbc);

        //scoreboard
        JButton highScore = new JButton("High Score");

        gbc.gridy = 4;
        highScore.setPreferredSize(new Dimension(250, 50));
        styleButton(highScore);
        add(highScore, gbc);

        //kilepes
        JButton exitGame = new JButton("Exit");
        gbc.gridy = 5;
        exitGame.setPreferredSize(new Dimension(150, 50));
        styleButton(exitGame);
        add(exitGame, gbc);

        saveScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!scoreSaved) {
                    String playerName = JOptionPane.showInputDialog(
                            VictoryPanel.this,
                            "Name:",
                            "Save Score",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (playerName != null && !playerName.trim().isEmpty()) {
                        gf.saveScores(playerName.trim(), score);
                        scoreSaved = true;
                    }
                }


            }
        });
        restartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gf.showPanel("Game");
                gf.restartGame();
                gf.getGlassPane().setVisible(false);
            }
        });

        highScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gf.showPanel("High Score");
                gf.getGlassPane().setVisible(false);
            }
        });

        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public void styleButton(JButton b) {
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setForeground(Color.white);
        b.setBorderPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 40));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // sötét áttetsző réteg
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // középső panel
        int w = 100;
        int h = 50;
        int x = (getWidth() - w) / 3;
        int y = (getHeight() - h) / 3;
    }

}
