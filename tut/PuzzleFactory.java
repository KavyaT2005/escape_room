public class PuzzleFactory {
    public static Puzzle createPuzzle(String type, String id, String question, String answer, String hint) {
        switch (type.toLowerCase()) {
            case "riddle":
                return new RiddlePuzzle(id, question, answer, hint);
            case "combination":
                return new CombinationPuzzle(id, question, answer, hint);
            default:
                throw new IllegalArgumentException("Unknown puzzle type: " + type);
        }
    }
}