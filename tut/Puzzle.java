public abstract class Puzzle {
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

    // ✅ Add "throws PuzzleSolveException" here
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
    public String getHint() { return hint; }
}