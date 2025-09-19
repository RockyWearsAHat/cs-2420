package assign02;

public final class ScoreEntry {

    private final double score;
    private final ScoreCategory category;

    ScoreEntry(double score, ScoreCategory category) {
        this.score = score;
        this.category = category;
    }

    double getScore() {
        return score;
    }

    ScoreCategory getCategory() {
        return category;
    }
}
