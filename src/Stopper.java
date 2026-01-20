import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stopper extends JPanel {
    private int secsSinceStart = 90;
    private final Timer timerSeconds;
    private final JLabel label;

    public Stopper(GameFrame gf, GameModel gm) {
        label = new JLabel(Integer.toString(secsSinceStart));
        setLayout(new FlowLayout());
        label.setForeground(Color.white);
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setOpaque(false);
        add(label);
        setOpaque(false);

        timerSeconds = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secsSinceStart--;
                label.setText(Integer.toString(secsSinceStart));
                if (secsSinceStart == 0) {
                    gf.showDefeat(gm.getScore());
                    timerStop();
                }
            }
        });
        timerSeconds.start();
        setBounds(100, 100, 500, 500);
        setVisible(true);
    }

    public void timerStop() {
        timerSeconds.stop();
    }

    public void timerReset() {
        secsSinceStart = 90;
        label.setText("90");
        timerSeconds.start();
    }
}
