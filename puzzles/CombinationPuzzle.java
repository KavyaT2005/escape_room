package puzzles;

import exceptions.PuzzleSolveException;

public class CombinationPuzzle extends Puzzle {

    public CombinationPuzzle(String id, String question, String answer, String hint) {
        super(id, question, answer, hint);
    }

    @Override
    public boolean attemptSolve(String attempt) throws PuzzleSolveException {
        if (attempt.equals(getAnswer())) {
            setSolved(true);
            return true;
        } else {
            throw new PuzzleSolveException("Incorrect combination!");
        }
    }
}
