import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {
    private final List<Score> scoreBoard;
    private static final String FILE = "ScoreBoard.txt";

    public ScoreManager() {
        this.scoreBoard = new ArrayList<Score>();
        loadScoreBoard();
    }

    public void addScore(String name, int score) {
        scoreBoard.add(new Score(name, score));
        scoreBoard.sort(Comparator.comparingInt(Score::getScore).reversed());
        saveScoreBoard();
    }

    public List<Score> getTopScores(int limit) {
        return scoreBoard.stream()
                .limit(limit)
                .toList();
    }

    public List<Score> getScoreBoard() {
        return List.copyOf(scoreBoard);
    }

    private void saveScoreBoard() {
        try {
            saveScoreBoardToFile(new File(FILE));
        } catch (IOException e) {
            System.err.println("Hiba a pontszámok mentésekor: " + e.getMessage());
        }
    }

    public void saveScoreBoardToFile(File file) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Score s : scoreBoard) {
                writer.write(s.getName() + " " + s.getScore() + "\n");
            }
        }
    }

    public void loadScoreBoard() {
        File newFile = new File(FILE);
        if (!newFile.exists()) {
            return;
        }
        try {
            loadScoreBoardFromFile(newFile);
        } catch (IOException e) {
            System.err.println("Hiba a pontszámok betöltésekor: " + e.getMessage());
        }
    }

    public void loadScoreBoardFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String parts[] = line.split(" ");

                if (parts.length != 2) {
                    continue;
                }

                String name = parts[0];
                int score;

                try {
                    score = Integer.parseInt(parts[1]);
                    scoreBoard.add(new Score(name, score));
                } catch (NumberFormatException e) {
                    System.err.println("Hiba a pontszámok betöltésekor: " + e.getMessage());
                }

            }
        }
        scoreBoard.sort(Comparator.comparingInt(Score::getScore).reversed());
    }

}
