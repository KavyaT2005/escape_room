package rooms;
public class LockedRoom extends Room {
    private String requiredPuzzleId;
    private boolean locked;
    private boolean explored;

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

    // ✅ FIXED: Implement abstract method canEnter()
    @Override
    public boolean canEnter() {
        // Check if the required puzzle is solved anywhere in the game
        return !locked || isPuzzleCompleted(requiredPuzzleId);
    }

    // ✅ FIXED: Implement abstract method onEnter()
    @Override
    public void onEnter() {
        if (locked) {
            // Check if puzzle is solved now
            if (isPuzzleCompleted(requiredPuzzleId)) {
                locked = false;
                System.out.println("🔓 The door unlocks! The puzzle has been solved!");
            } else {
                System.out.println("🚪 The door is locked! You need to solve puzzle: " + requiredPuzzleId);
                return; // Don't proceed with entry
            }
        }
        
        // If we get here, either it was never locked or is now unlocked
        if (!explored) {
            System.out.println("You enter " + getName() + " for the first time!");
            explored = true;
        } else {
            System.out.println("You return to " + getName());
        }
        
        System.out.println(getDescription());
    }

    // ✅ Method to force unlock (for debugging)
    public void forceUnlock() {
        this.locked = false;
        System.out.println("🔓 Room force unlocked!");
    }

    public boolean isExplored() {
        return explored;
    }

    @Override
    public String toString() {
        return getName() + " [Locked: " + locked + ", Requires: " + requiredPuzzleId + "]";
    }
}