import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final CardLayout l;
    private final JPanel cardPanel;
    private final GameModel gm;
    private final BattleshipPanel battleshipPanel;
    private final ScoreManager scoreManager;
    private final HighScorePanel highScorePanel;

    public GameFrame() {
        gm = new GameModel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Battleship");

        l = new CardLayout();
        cardPanel = new JPanel(l);

        Stopper stopper = new Stopper(this, gm);
        //itt alapbol a main panel jelenik meg, es azt lehet majd valtoztatni
        MainMenuPanel mainPanel = new MainMenuPanel(this);
        battleshipPanel = new BattleshipPanel(gm, this, stopper);
        scoreManager = new ScoreManager();
        highScorePanel = new HighScorePanel(scoreManager);

        cardPanel.add(mainPanel, "Menu");
        cardPanel.add(battleshipPanel, "Game");
        cardPanel.add(highScorePanel, "High Score");

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void showPanel(String name) {
        if (name.equals("High Score")) {
            highScorePanel.updateScores(scoreManager);
        }
        l.show(cardPanel, name);
    }

    public void restartGame() {
        gm.resetGame();                 // új hajók
        battleshipPanel.resetGame();   // UI reset
        getGlassPane().setVisible(false);
        showPanel("Game");
    }

    public void showVictory(int score) {
        VictoryPanel vp = new VictoryPanel(this, score);
        setGlassPane(vp);
        vp.setVisible(true);
    }

    public void showDefeat(int score) {
        DefeatPanel dp = new DefeatPanel(this, score);
        setGlassPane(dp);
        dp.setVisible(true);
    }

    public void saveScores(String name, int score) {
        scoreManager.addScore(name, score);
    }

    public static void main(String[] args) {
        new GameFrame();
    }

}



