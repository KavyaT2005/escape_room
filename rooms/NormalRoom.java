package rooms;

public class NormalRoom extends Room {
    private boolean explored;

    public NormalRoom(String id, String name, String desc) {
        super(id, name, desc);
        this.explored = false;
    }

    // ✅ Implement abstract method canEnter()
    @Override
    public boolean canEnter() {
        return true; // Normal rooms can always be entered
    }

    // ✅ Implement abstract method onEnter()
    @Override
    public void onEnter() {
        if (!explored) {
            System.out.println("You enter " + getName() + " for the first time!");
            System.out.println(getDescription());
            explored = true;
        } else {
            System.out.println("You return to " + getName());
        }
        
        // Show room contents
        interact();
    }

    // ✅ Helper method to check if room has been explored
    public boolean isExplored() {
        return explored;
    }

    // ✅ Optional: Method to mark as explored
    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    // ✅ Optional: Override interact for better display
    @Override
    public void interact() {
        System.out.println("=== " + getName() + " ===");
        System.out.println(getDescription());
        
        // Show items
        if (!getItems().isEmpty()) {
            System.out.println("📦 Items here: " + getItems());
        } else {
            System.out.println("No items in this room.");
        }
        
        // Show puzzles
        if (!getPuzzles().isEmpty()) {
            System.out.println("❓ Puzzles here:");
            for (puzzles.Puzzle puzzle : getPuzzles()) {
                System.out.println("   - " + puzzle.getId() + ": " + 
                    (puzzle.isSolved() ? "✅ SOLVED" : "❌ UNSOLVED"));
            }
        }
        
        // Show exits
        if (!getConnections().isEmpty()) {
            System.out.println("🚪 Exits: " + getConnections().keySet());
        }
    }

    @Override
    public String toString() {
        return getName() + " [Explored: " + explored + "]";
    }
}