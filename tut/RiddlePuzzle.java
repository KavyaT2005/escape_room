public class RiddlePuzzle extends Puzzle {
    public RiddlePuzzle(String id, String question, String answer, String hint) {
        super(id, question, answer, hint);
    }

    @Override
    public boolean attemptSolve(String attempt) throws PuzzleSolveException {
        if (attempt.equalsIgnoreCase(getAnswer())) {
            setSolved(true);
            return true;
        } else {
            throw new PuzzleSolveException("Wrong answer to the riddle!");
        }
    }
}