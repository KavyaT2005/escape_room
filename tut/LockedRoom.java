public class LockedRoom extends Room {
    private String requiredPuzzleId;
    private boolean locked;
    private boolean explored;

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        LockedRoom testRoom = new LockedRoom("test1", "Test Locked Room", "A test locked room", "puzzle1");
        System.out.println("LockedRoom created: " + testRoom.getName());
    }

    public LockedRoom(String id, String name, String desc, String puzzleId) {
        super(id, name, desc);
        this.requiredPuzzleId = puzzleId;
        this.locked = true;
        this.explored = false;
    }

    public String getRequiredPuzzleId() {
        return requiredPuzzleId;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean canEnter() {
        return !locked || isPuzzleCompleted(requiredPuzzleId);
    }

    @Override
    public void onEnter() {
        if (locked) {
            if (isPuzzleCompleted(requiredPuzzleId)) {
                locked = false;
                System.out.println("The door unlocks! The puzzle has been solved!");
            } else {
                System.out.println("The door is locked! You need to solve puzzle: " + requiredPuzzleId);
                return;
            }
        }

        if (!explored) {
            System.out.println("You enter " + getName() + " for the first time!");
            explored = true;
        } else {
            System.out.println("You return to " + getName());
        }

        System.out.println(getDescription());
        interact();
    }

    public void forceUnlock() {
        this.locked = false;
        System.out.println("Room force unlocked!");
    }

    public boolean isExplored() {
        return explored;
    }

    @Override
    public void interact() {
        System.out.println("=== " + getName() + " (Locked Room) ===");
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
        return getName() + " [Locked: " + locked + ", Requires: " + requiredPuzzleId + "]";
    }
}