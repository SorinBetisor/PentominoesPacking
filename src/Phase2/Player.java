package Phase2;

public class Player {

    //comment
    private String name;
    private int currentScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public Player(String name, int currentScore) {
        this.name = name;
        this.currentScore = currentScore;
    }
}
