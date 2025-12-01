public class NormalRoom extends Room {
    private boolean explored;

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        NormalRoom testRoom = new NormalRoom("test1", "Test Room", "A test room");
        System.out.println("NormalRoom created: " + testRoom.getName());
    }

    public NormalRoom(String id, String name, String desc) {
        super(id, name, desc);
        this.explored = false;
    }

    @Override
    public boolean canEnter() {
        return true;
    }

    @Override
    public void onEnter() {
        if (!explored) {
            System.out.println("You enter " + getName() + " for the first time!");
            System.out.println(getDescription());
            explored = true;
        } else {
            System.out.println("You return to " + getName());
        }
        interact();
    }

    public boolean isExplored() {
        return explored;
    }

    @Override
    public void interact() {
        System.out.println("=== " + getName() + " ===");
        System.out.println(getDescription());

        if (!getItems().isEmpty()) {
            System.out.println("  Items here: " + getItems());
        } else {
            System.out.println("No items in this room.");
        }

        if (!getPuzzles().isEmpty()) {
            System.out.println(" ? Puzzles here:");
            for (Puzzle puzzle : getPuzzles()) {
                System.out.println(" - " + puzzle.getId() + ": " + (puzzle.isSolved() ? "SOLVED" : "UNSOLVED"));
            }
        }

        if (!getConnections().isEmpty()) {
            System.out.println("  Exits: " + getConnections().keySet());
        }
    }

    @Override
    public String toString() {
        return getName() + " [Explored: " + explored + "]";
    }
}