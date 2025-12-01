package puzzles;

import exceptions.PuzzleSolveException;
import interfaces.Interactive;

public abstract class Puzzle implements Interactive {
    protected boolean solved;
    private String id;
    private String question;
    private String answer;
    private String hint;

    public Puzzle(String id, String question, String answer, String hint) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        this.solved = false;
    }

    public abstract boolean attemptSolve(String attempt) throws PuzzleSolveException;

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isSolved() {
        return solved;
    }

    public String getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getHint() { return hint; } // ✅ Added this method

    @Override
    public void interact() {
        System.out.println("Puzzle: " + question);
        System.out.println("Hint: " + hint);
        System.out.println("Status: " + (solved ? "SOLVED" : "UNSOLVED"));
    }

    @Override
    public String getDescription() {
        return question;
    }

    @Override
    public boolean isActive() {
        return !solved;
    }
}