package assign02;

import java.util.ArrayList;

public class CS2420StudentGeneric<Type> extends UofUStudent {

    // Embedded helper types so autograder doesn't depend on separate files
    private enum ScoreCategory {
        ASSIGNMENT, EXAM, LAB, QUIZ, POLL
    }

    private static final class ScoreEntry {

        private final double score;
        private final ScoreCategory category;

        private ScoreEntry(double score, ScoreCategory category) {
            this.score = score;
            this.category = category;
        }

        private double getScore() {
            return score;
        }

        private ScoreCategory getCategory() {
            return category;
        }
    }

    private final Type contactInfo;
    private final ArrayList<ScoreEntry> scores;

    public CS2420StudentGeneric(String firstName, String lastName, int uNID, Type contactInfo) {
        super(firstName, lastName, uNID);
        this.contactInfo = contactInfo;
        this.scores = new ArrayList<>();
    }

    public Type getContactInfo() {
        return this.contactInfo;
    }

    public void addScore(double score, String category) {
        this.scores.add(new ScoreEntry(score, ScoreCategory.valueOf(category.toUpperCase())));
    }

    public double computeFinalScore() {
        //Assignments 30%, Exams 53%, Labs 7%, Quizzes 5%, In-Class Polling 5%
        double assignmentTotal = 0, examTotal = 0, labTotal = 0, quizTotal = 0, pollingTotal = 0;
        int numAssignments = 0, numExams = 0, numLabs = 0, numQuizzes = 0, numPolls = 0;

        for (ScoreEntry entry : scores) {
            switch (entry.getCategory()) {
                case ASSIGNMENT -> {
                    assignmentTotal += entry.getScore();
                    numAssignments++;
                }
                case EXAM -> {
                    examTotal += entry.getScore();
                    numExams++;
                }
                case LAB -> {
                    labTotal += entry.getScore();
                    numLabs++;
                }
                case QUIZ -> {
                    quizTotal += entry.getScore();
                    numQuizzes++;
                }
                case POLL -> {
                    pollingTotal += entry.getScore();
                    numPolls++;
                }
            }
        }

        //Idk what I was thinking here but failed a test
        // if (assignmentTotal == 0 || examTotal == 0 || labTotal == 0 || quizTotal == 0 || pollingTotal == 0) {
        //     return 0.0;
        // }
        double finalScore
                = ((assignmentTotal / (numAssignments * 100)) * 30)
                + ((examTotal / (numExams * 100)) * 53)
                + ((labTotal / (numLabs * 100)) * 7)
                + ((quizTotal / (numQuizzes * 100)) * 5)
                + ((pollingTotal / (numPolls * 100)) * 5);

        if (Double.isNaN(finalScore) || finalScore < 0) {
            return 0.0;
        }
        return finalScore;
    }

    public String computeFinalGrade() {
        double finalScore = computeFinalScore();
        if (finalScore == 0) {
            return "N/A";
        }

        if (finalScore >= 93) {
            return "A";
        } else if (finalScore >= 90) {
            return "A-";
        } else if (finalScore >= 87) {
            return "B+";
        } else if (finalScore >= 83) {
            return "B";
        } else if (finalScore >= 80) {
            return "B-";
        } else if (finalScore >= 77) {
            return "C+";
        } else if (finalScore >= 73) {
            return "C";
        } else if (finalScore >= 70) {
            return "C-";
        } else if (finalScore >= 67) {
            return "D+";
        } else if (finalScore >= 63) {
            return "D";
        } else if (finalScore >= 60) {
            return "D-";
        } else {
            return "E";
        }
    }
}
