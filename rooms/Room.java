package rooms;

import items.Item;
import puzzles.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public abstract class Room {
    private String id;
    private String name;
    private String description;
    private List<Item> items = new ArrayList<>();
    private List<Puzzle> puzzles = new ArrayList<>();
    private java.util.Map<String, Room> connections = new java.util.HashMap<>();
    private Set<String> completedPuzzles = new HashSet<>(); // ✅ Added missing field

    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void addItem(Item item) { items.add(item); }
    public List<Item> getItems() { return items; }
    
    public Item removeItem(String name) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(name)) {
                items.remove(i);
                return i;
            }
        }
        return null;
    }

    public void addPuzzle(Puzzle puzzle) { puzzles.add(puzzle); }
    public List<Puzzle> getPuzzles() { return puzzles; }

    public void addConnection(String direction, Room room) { 
        connections.put(direction.toLowerCase(), room); 
    }
    
    public Room getConnectedRoom(String direction) { 
        return connections.get(direction.toLowerCase()); 
    }

    // ✅ Get all connections
    public java.util.Map<String, Room> getConnections() {
        return Collections.unmodifiableMap(connections);
    }

    // ✅ Added getConnection method (alias for getConnectedRoom)
    public Room getConnection(String direction) {
        return connections.get(direction.toLowerCase());
    }

    // ✅ Added completed puzzles management
    public void markPuzzleCompleted(String puzzleId) {
        completedPuzzles.add(puzzleId);
        // Also mark the puzzle itself as solved
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getId().equals(puzzleId)) {
                // Use reflection to call setSolved method
                try {
                    java.lang.reflect.Method setSolvedMethod = puzzle.getClass().getMethod("setSolved", boolean.class);
                    setSolvedMethod.invoke(puzzle, true);
                } catch (Exception e) {
                    // If reflection fails, try direct method call
                    try {
                        puzzle.attemptSolve("bypass"); // Some puzzles might accept this
                    } catch (Exception ex) {
                        // If all fails, we can't mark it as solved
                        System.out.println("Could not mark puzzle as solved: " + puzzleId);
                    }
                }
                break;
            }
        }
    }

    // ✅ Get completed puzzles
    public Set<String> getCompletedPuzzles() {
        return Collections.unmodifiableSet(completedPuzzles);
    }

    // ✅ Check if puzzle is completed
    public boolean isPuzzleCompleted(String puzzleId) {
        return completedPuzzles.contains(puzzleId);
    }

    // ✅ Abstract methods to be implemented by subclasses
    public abstract boolean canEnter();
    public abstract void onEnter();

    // ✅ Interact method
    public void interact() {
        System.out.println("You are in " + name + ": " + description);
        if (!items.isEmpty()) {
            System.out.println("Items here: " + items);
        } else {
            System.out.println("No items in this room.");
        }
        if (!puzzles.isEmpty()) {
            System.out.println("Puzzles here: " + puzzles.size());
            for (Puzzle puzzle : puzzles) {
                System.out.println(" - " + puzzle.getId() + ": " + 
                    (puzzle.isSolved() ? "SOLVED" : "UNSOLVED"));
            }
        } else {
            System.out.println("No puzzles in this room.");
        }
        
        // Show available exits
        if (!connections.isEmpty()) {
            System.out.println("Exits: " + String.join(", ", connections.keySet()));
        }
    }

    // ✅ Additional helper methods
    public boolean hasItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPuzzle(String puzzleId) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getId().equals(puzzleId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}