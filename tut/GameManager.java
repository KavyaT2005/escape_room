import java.util.*;

public class GameManager {
    private static GameManager instance;
    private Map<String, Room> rooms = new HashMap<>();
    private Room currentRoom;
    private List<Item> playerInventory = new ArrayList<>();
    private boolean gameRunning = false;

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        System.out.println("GameManager class loaded successfully!");
        System.out.println("Run EscapeRoomGUI to play the game.");
    }

    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    public void startGame(Room startingRoom) {
        this.currentRoom = startingRoom;
        this.gameRunning = true;
        this.playerInventory.clear();
        System.out.println("Game started!");
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public List<Item> getPlayerInventory() {
        return Collections.unmodifiableList(playerInventory);
    }

    public void move(String direction) throws GameException {
        if (!gameRunning) {
            throw new GameException("Game is not running!");
        }

        Room nextRoom = currentRoom.getConnection(direction);
        if (nextRoom == null) {
            throw new GameException("No room in that direction: " + direction);
        }
        
        if (!nextRoom.canEnter()) {
            throw new GameException("This room is locked! Solve the puzzle first.");
        }
        
        currentRoom = nextRoom;
        currentRoom.onEnter();
    }

    public void pickUpItem(String itemName) throws GameException {
        if (!gameRunning) {
            throw new GameException("Game is not running!");
        }

        Item item = currentRoom.removeItem(itemName);
        if (item == null) {
            throw new GameException("No such item in this room: " + itemName);
        }
        playerInventory.add(item);
    }

    public void solvePuzzle(String puzzleId, String solution) throws GameException {
        if (!gameRunning) {
            throw new GameException("Game is not running!");
        }

        Puzzle puzzle = getPuzzleById(puzzleId);
        if (puzzle == null) {
            throw new GameException("Puzzle not found: " + puzzleId);
        }

        if (puzzle.isSolved()) {
            throw new GameException("Puzzle is already solved: " + puzzleId);
        }
        
        if (puzzle.attemptSolve(solution)) {
            currentRoom.markPuzzleCompleted(puzzleId);
        } else {
            throw new GameException("Wrong solution for puzzle: " + puzzleId);
        }
    }

    private Puzzle getPuzzleById(String puzzleId) {
        for (Room room : rooms.values()) {
            for (Puzzle p : room.getPuzzles()) {
                if (p.getId().equals(puzzleId)) {
                    return p;
                }
            }
        }
        return null;
    }

    public void useItem(String itemId) throws GameException {
        Item item = getItemFromInventory(itemId);
        if (item == null) {
            throw new GameException("Item not in inventory: " + itemId);
        }
        // Item usage logic here
    }

    private Item getItemFromInventory(String itemId) {
        for (Item item : playerInventory) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
}