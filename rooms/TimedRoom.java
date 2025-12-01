package rooms;

public class TimedRoom extends Room {
    private int timeLimit;
    private long enterTime;
    private boolean timerActive;
    private boolean explored;
    private boolean failed;

    public TimedRoom(String id, String name, String desc, int timeLimit) {
        super(id, name, desc);
        this.timeLimit = timeLimit;
        this.timerActive = false;
        this.explored = false;
        this.failed = false;
    }

    // ✅ Implement abstract method canEnter()
    @Override
    public boolean canEnter() {
        return !failed; // Can enter if not failed yet
    }

    // ✅ Implement abstract method onEnter()
    @Override
    public void onEnter() {
        if (failed) {
            System.out.println("⏰ Time's up! You failed to escape this room in time.");
            return;
        }

        if (!timerActive) {
            enterTime = System.currentTimeMillis();
            timerActive = true;
            System.out.println("⏰ TIMED ROOM: You have " + timeLimit + " seconds to escape!");
            System.out.println(getDescription());
        }

        if (!explored) {
            explored = true;
        }

        // Check if time is already up
        if (isTimeUp()) {
            failed = true;
            System.out.println("💀 TIME'S UP! You failed to escape in time.");
        } else {
            System.out.println("⏱️ Time remaining: " + getRemainingTime() + " seconds");
        }
    }

    // ✅ Check if time is up
    public boolean isTimeUp() {
        if (!timerActive) return false;
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - enterTime) / 1000;
        return elapsedSeconds >= timeLimit;
    }

    // ✅ Get remaining time
    public int getRemainingTime() {
        if (!timerActive) return timeLimit;
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - enterTime) / 1000;
        return Math.max(0, timeLimit - (int)elapsedSeconds);
    }

    // ✅ Getter methods
    public int getTimeLimit() {
        return timeLimit;
    }

    public boolean isTimerActive() {
        return timerActive;
    }

    public boolean hasFailed() {
        return failed;
    }

    public boolean isExplored() {
        return explored;
    }

    // ✅ Reset timer (for game restarts)
    public void resetTimer() {
        timerActive = false;
        failed = false;
        enterTime = 0;
    }

    // ✅ Start timer manually
    public void startTimer() {
        if (!timerActive) {
            enterTime = System.currentTimeMillis();
            timerActive = true;
            failed = false;
        }
    }

    // ✅ Override interact to show timer status
    @Override
    public void interact() {
        System.out.println("=== " + getName() + " (Timed Room) ===");
        System.out.println(getDescription());
        
        if (timerActive && !failed) {
            System.out.println("⏱️ Time remaining: " + getRemainingTime() + " / " + timeLimit + " seconds");
        } else if (failed) {
            System.out.println("💀 FAILED - Time's up!");
        } else {
            System.out.println("⏰ Time limit: " + timeLimit + " seconds");
        }
        
        // Show items
        if (!getItems().isEmpty()) {
            System.out.println("📦 Items here: " + getItems());
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
        return getName() + " [Timed: " + timeLimit + "s, Active: " + timerActive + ", Failed: " + failed + "]";
    }
}