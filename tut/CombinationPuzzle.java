public class CombinationPuzzle extends Puzzle {
    public CombinationPuzzle(String id, String question, String answer, String hint) {
        super(id, question, answer, hint);
    }

    @Override
    public boolean attemptSolve(String attempt) throws PuzzleSolveException {
        if (attempt == null) {
            throw new PuzzleSolveException("Attempt cannot be null!");
        }
        
        if (attempt.equals(getAnswer())) {
            setSolved(true);
            return true;
        } else {
            throw new PuzzleSolveException("Incorrect combination!");
        }
    }
}