package hu.elte.fi.progtech.snake.model.score;

public class ScoreBoard {

    private int score;
    private String user;
    private int[] topScore;
    private String[] topPlayer;
    private boolean gameOver = true;

    public void reset(String user) {
        setTopScore();
        score = 0;
        this.user = user;
        gameOver = false;
    }

    public void setGameOver() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setTopScore(int[] topScore, String[] topPlayer) {
        this.topScore = topScore;
        this.topPlayer = topPlayer;
    }

    public void setTopScore() {
        if(topScore.length == 0) {
            return;
        }

        if(score > topScore[9]) {
            topScore[9] = score;
            topPlayer[9] = user;
        }
        for(int i = 8; i >= 0; --i ) {
            if(score > topScore[i]) {
                int temp = topScore[i];
                String temp2 = topPlayer[i];
                topScore[i] = score;
                topScore[i + 1] = temp;
                topPlayer[i] = user;
                topPlayer[i + 1] = temp2;
            } else {
                break;
            }
        }
    }

    public int[] getTopScore() {
        return topScore;
    }

    public String[] getTopPlayer() {
        return topPlayer;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }


}

