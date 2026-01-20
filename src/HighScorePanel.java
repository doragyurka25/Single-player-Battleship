import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HighScorePanel extends JPanel {
    private BufferedImage img;
    private final JTextArea scoreArea;

    public HighScorePanel(ScoreManager sm) {
        try {
            img = ImageIO.read(new File("img/battleship.png"));
        } catch (IOException e) {
            System.out.println("Nem lehetett beolvasni a kepet\n");
        }
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Score Board");
        label.setFont(new Font("Arial", Font.BOLD, 70));
        label.setForeground(Color.white);
        add(label, BorderLayout.NORTH);

        this.setPreferredSize(new Dimension(1200, 1000));

        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setOpaque(false);
        scoreArea.setForeground(Color.WHITE);
        scoreArea.setFont(new Font("Monospaced", Font.BOLD, 30));
        scoreArea.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 50));

        add(scoreArea, BorderLayout.CENTER);
        updateScores(sm);
    }

    public void updateScores(ScoreManager sm) {
        StringBuilder sb = new StringBuilder();
        int rank = 1;

        for (Score s : sm.getTopScores(10)) {
            sb.append(rank++)
                    .append(". ")
                    .append(s.getName())
                    .append(" - ")
                    .append(s.getScore())
                    .append("\n");
        }
        scoreArea.setText(sb.toString());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // kirajzolom a hatteret
        g.drawImage(img, 0, 0, 1200, 1000, null);

    }
}
