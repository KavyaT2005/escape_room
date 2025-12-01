import java.util.*;

public abstract class Room {
    private String id;
    private String name;
    private String description;
    private List<Item> items = new ArrayList<>();
    private List<Puzzle> puzzles = new ArrayList<>();
    private Map<String, Room> connections = new HashMap<>();
    private Set<String> completedPuzzles = new HashSet<>();

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        System.out.println("Room base class loaded successfully!");
    }

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

    public Room getConnection(String direction) {
        return connections.get(direction.toLowerCase());
    }

    public Map<String, Room> getConnections() {
        return Collections.unmodifiableMap(connections);
    }

    public void markPuzzleCompleted(String puzzleId) {
        completedPuzzles.add(puzzleId);
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getId().equals(puzzleId)) {
                puzzle.setSolved(true);
                break;
            }
        }
    }

    public Set<String> getCompletedPuzzles() {
        return Collections.unmodifiableSet(completedPuzzles);
    }

    public boolean isPuzzleCompleted(String puzzleId) {
        return completedPuzzles.contains(puzzleId);
    }

    public abstract boolean canEnter();
    public abstract void onEnter();
    public abstract void interact();
}