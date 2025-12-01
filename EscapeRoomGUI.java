// EscapeRoomGUI.java (Updated)
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import patterns.GameManager;
import rooms.Room;
import items.Item;
import puzzles.Puzzle;
import exceptions.GameException;

public class EscapeRoomGUI extends JFrame {
    private GameManager gameManager;
    private Timer gameTimer;
    private int timeRemaining = 300; // 5 minutes in seconds
    private boolean gameActive = true;
    private String currentPlayer;
    private RankingManager rankingManager;

    // ... (previous UI components remain same)

    public EscapeRoomGUI(String playerName) {
        this.currentPlayer = playerName;
        this.rankingManager = new RankingManager();
        gameManager = GameManager.getInstance();
        initializeGame();
        initializeGUI();
        startGameTimer();
        gameManager.startGame(gameManager.getRoom("room1"));
        updateGameDisplay();
        
        // Welcome message
        addToLog("🎮 Welcome, " + playerName + "! 🎮");
        addToLog("You have 5 minutes to escape!");
    }

    private void initializeGame() {
        // Create items
        Item key = new Item("key1", "Rusty Key", "An old rusty key", true);
        Item note = new Item("note1", "Mysterious Note", "A note with strange symbols", true);
        Item torch = new Item("torch1", "Golden Torch", "A shining golden torch", true);

        // Create puzzles with enhanced riddles
        Puzzle riddle = new puzzles.RiddlePuzzle("riddle1",
            "I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I?",
            "echo", "Think about what repeats sounds in empty spaces");

        Puzzle combination = new puzzles.CombinationPuzzle("combo1",
            "A locked safe with a 3-digit combination. The note says: 'Start from the beginning'",
            "123", "Try the simplest sequence");

        // Create rooms
        Room entrance = new rooms.NormalRoom("room1", "Entrance Hall",
            "A grand entrance hall with marble floors. There's a mysterious note on the table.");
        
        Room library = new rooms.LockedRoom("room2", "Ancient Library",
            "A dusty library filled with old books. A riddle is inscribed on the wall.", "riddle1");
        
        Room treasure = new rooms.LockedRoom("room3", "Treasure Room",
            "A room filled with gold and jewels! A safe sits in the center.", "combo1");

        // Setup connections
        entrance.addConnection("north", library);
        library.addConnection("south", entrance);
        library.addConnection("east", treasure);
        treasure.addConnection("west", library);

        // Add items to rooms
        entrance.addItem(note);
        library.addItem(key);
        treasure.addItem(torch);

        // Add puzzles to rooms
        entrance.addPuzzle(riddle); // Riddle is in entrance but blocks library
        treasure.addPuzzle(combination);

        // Register rooms with game manager
        gameManager.addRoom(entrance);
        gameManager.addRoom(library);
        gameManager.addRoom(treasure);
    }

    // Enhanced movement handler with riddle checking
    private void handleMovement(String direction) throws GameException {
        Room currentRoom = gameManager.getCurrentRoom();
        Room nextRoom = currentRoom.getConnection(direction);
        
        if (nextRoom != null && nextRoom instanceof rooms.LockedRoom) {
            rooms.LockedRoom lockedRoom = (rooms.LockedRoom) nextRoom;
            
            if (!lockedRoom.canEnter()) {
                // Show riddle if room is locked
                showRiddlePrompt(lockedRoom.getRequiredPuzzleId());
                return;
            }
        }
        
        // If not locked or can enter, proceed with movement
        Room oldRoom = gameManager.getCurrentRoom();
        gameManager.move(direction);
        Room newRoom = gameManager.getCurrentRoom();

        addToLog("🎯 You move " + direction.toUpperCase() + "...");
        
        // Room-specific messages
        if (newRoom.getName().equals("Entrance Hall")) {
            addToLog("🏛️ You are in the ENTRANCE HALL");
            addToLog("💡 Look around for clues. There might be something useful here!");
        } else if (newRoom.getName().equals("Ancient Library")) {
            addToLog("📚 You enter the ANCIENT LIBRARY");
            addToLog("💡 The riddle on the wall glows mysteriously...");
        } else if (newRoom.getName().equals("Treasure Room")) {
            addToLog("💎 You enter the TREASURE ROOM!");
            addToLog("⚡ The final challenge awaits! Solve the combination lock!");
        }

        updateGameDisplay();
        checkWinCondition();
    }

    private void showRiddlePrompt(String puzzleId) {
        Puzzle puzzle = findPuzzleById(puzzleId);
        if (puzzle != null && !puzzle.isSolved()) {
            String solution = JOptionPane.showInputDialog(this,
                "🔐 ROOM LOCKED!\n\n" +
                "To proceed, solve this riddle:\n\n" +
                puzzle.getQuestion() + "\n\n" +
                "Hint: " + puzzle.getHint() + "\n\n" +
                "Enter your answer:",
                "Riddle Challenge",
                JOptionPane.QUESTION_MESSAGE);
            
            if (solution != null && !solution.trim().isEmpty()) {
                try {
                    gameManager.solvePuzzle(puzzleId, solution.trim());
                    if (puzzle.isSolved()) {
                        addToLog("🎉 Riddle solved! The door unlocks!");
                        JOptionPane.showMessageDialog(this,
                            "✅ CORRECT!\n\nThe door magically unlocks!",
                            "Riddle Solved",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        addToLog("❌ Wrong answer! Try again.");
                        JOptionPane.showMessageDialog(this,
                            "❌ Incorrect answer!\n\nTry again!",
                            "Wrong Answer",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (GameException e) {
                    addToLog("❌ " + e.getMessage());
                }
            }
        }
    }

    private Puzzle findPuzzleById(String puzzleId) {
        for (Room room : gameManager.getRooms()) {
            for (Puzzle puzzle : room.getPuzzles()) {
                if (puzzle.getId().equals(puzzleId)) {
                    return puzzle;
                }
            }
        }
        return null;
    }

    // Enhanced win condition
    private void winGame() {
        gameActive = false;
        gameTimer.stop();
        
        // Save score
        rankingManager.saveScore(currentPlayer, timeRemaining);
        
        addToLog("");
        addToLog("🎉🎉🎉 CONGRATULATIONS! 🎉🎉🎉");
        addToLog("🏆 YOU SUCCESSFULLY ESCAPED THE ROOM!");
        addToLog("⭐ Player: " + currentPlayer);
        addToLog("⏰ Time remaining: " + timeRemaining + " seconds");
        addToLog("📊 Score: " + timeRemaining + " points");
        addToLog("");
        addToLog("You are a true escape room master! 🎯");

        int choice = JOptionPane.showOptionDialog(this,
            "🎉 VICTORY! 🎉\n\n" +
            "Player: " + currentPlayer + "\n" +
            "Time Remaining: " + timeRemaining + " seconds\n" +
            "Score: " + timeRemaining + " points\n\n" +
            "You solved all puzzles and collected all items!\n" +
            "Well done, escape master! 🏆",
            "🎊 VICTORY! 🎊",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"View Rankings", "Play Again"},
            "View Rankings");
        
        if (choice == 0) {
            rankingManager.displayRankings();
        }
        
        // Option to play again
        if (choice == 1) {
            this.dispose();
            new GameLauncher();
        }
    }

    // Enhanced game over
    private void gameOver() {
        gameActive = false;
        gameTimer.stop();
        
        addToLog("");
        addToLog("💀💀💀 GAME OVER 💀💀💀");
        addToLog("⏰ Time ran out before you could escape!");
        addToLog("The treasure room collapsed around you...");
        addToLog("");
        addToLog("Better luck next time! 🔄");

        int choice = JOptionPane.showOptionDialog(this,
            "💀 TIME'S UP! GAME OVER! 💀\n\n" +
            "You didn't escape in time!\n" +
            "The treasure room collapsed...\n\n" +
            "Try again and be faster next time!",
            "💀 GAME OVER 💀",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            new String[]{"Try Again", "Exit"},
            "Try Again");
        
        if (choice == 0) {
            this.dispose();
            new GameLauncher();
        } else {
            System.exit(0);
        }
    }

    // Add this method to GameManager or use reflection to access rooms
    private java.util.Collection<Room> getRooms() {
        // This is a helper method - you might need to modify GameManager
        // to provide access to all rooms
        try {
            java.lang.reflect.Field roomsField = gameManager.getClass().getDeclaredField("rooms");
            roomsField.setAccessible(true);
            Map<String, Room> roomsMap = (Map<String, Room>) roomsField.get(gameManager);
            return roomsMap.values();
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }
    // ... (rest of the previous methods remain similar with minor enhancements)
}