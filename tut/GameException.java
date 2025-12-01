public class GameException extends Exception {
    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        System.out.println("GameException class loaded successfully!");
    }

    public GameException(String message) {
        super(message);
    }
}