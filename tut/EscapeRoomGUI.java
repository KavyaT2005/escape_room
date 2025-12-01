import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EscapeRoomGUI extends JFrame {
    private GameManager gameManager;
    private javax.swing.Timer gameTimer;
    private int timeRemaining = 300;
    private boolean gameActive = true;
    private final String currentPlayer;

    private JTextArea roomDisplay;
    private JTextArea gameLog;
    private JTextField commandInput;
    private JButton sendButton;
    private JPanel inventoryPanel;
    private JPanel controlsPanel;
    private JLabel timerLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Escape Room Adventure", JOptionPane.QUESTION_MESSAGE);
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Player";
            }
            new EscapeRoomGUI(playerName.trim());
        });
    }

    public EscapeRoomGUI(String playerName) {
        this.currentPlayer = playerName;
        gameManager = new GameManager();
        initializeGame();
        initializeGUI();
        startGameTimer();
        gameManager.startGame(gameManager.getRoom("room1"));
        updateGameDisplay();
        
        addToLog("🎮 Welcome, " + playerName + "! 🎮");
        addToLog("You have 5 minutes to escape!");
    }

    private void initializeGame() {
        // Create items
        Item note = new Item("note1", "Mysterious Note", "A note with strange symbols", true);
        Item key = new Item("key1", "Rusty Key", "An old rusty key", true);
        Item torch = new Item("torch1", "Golden Torch", "A shining golden torch", true);

        // Create puzzles
        Puzzle riddle = new SimplePuzzle("riddle1",
            "I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I?",
            "echo", "Think about what repeats sounds in empty spaces");

        Puzzle combination = new SimplePuzzle("combo1",
            "A locked safe with a 3-digit combination. The note says: 'Start from the beginning'",
            "123", "Try the simplest sequence");

        // Create rooms
        Room entrance = new NormalRoom("room1", "Entrance Hall",
            "A grand entrance hall with marble floors. There's a mysterious note on the table.");
        
        Room library = new LockedRoom("room2", "Ancient Library",
            "A dusty library filled with old books. A riddle is inscribed on the wall.", "riddle1");
        
        Room treasure = new LockedRoom("room3", "Treasure Room",
            "A room filled with gold and jewels! A safe sits in the center.", "combo1");

        // Connect rooms
        entrance.addConnection("north", library);
        library.addConnection("south", entrance);
        library.addConnection("east", treasure);
        treasure.addConnection("west", library);

        // Add items to rooms
        entrance.addItem(note);
        library.addItem(key);
        treasure.addItem(torch);

        // Add puzzles to rooms
        library.addPuzzle(riddle);
        treasure.addPuzzle(combination);

        // Register rooms with game manager
        gameManager.addRoom(entrance);
        gameManager.addRoom(library);
        gameManager.addRoom(treasure);
    }

    private void initializeGUI() {  
        setTitle("Escape Room Adventure - Player: " + currentPlayer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        timerLabel = new JLabel("Time: 300s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(timerLabel, BorderLayout.EAST);

        JLabel titleLabel = new JLabel("Escape Room Adventure - " + currentPlayer, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Room display
        roomDisplay = new JTextArea(8, 50);  
        roomDisplay.setEditable(false);  
        roomDisplay.setBackground(Color.BLACK);  
        roomDisplay.setForeground(Color.GREEN);  
        roomDisplay.setFont(new Font("Courier New", Font.PLAIN, 14));  
        roomDisplay.setBorder(BorderFactory.createTitledBorder("Current Room"));
        add(new JScrollPane(roomDisplay), BorderLayout.CENTER);

        // Center panel with game log and inventory
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        gameLog = new JTextArea(12, 30);  
        gameLog.setEditable(false);  
        gameLog.setBackground(Color.DARK_GRAY);
        gameLog.setForeground(Color.WHITE);
        gameLog.setFont(new Font("Courier New", Font.PLAIN, 12));
        gameLog.setBorder(BorderFactory.createTitledBorder("Game Log"));
        centerPanel.add(new JScrollPane(gameLog));

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        updateInventoryDisplay();
        centerPanel.add(new JScrollPane(inventoryPanel));

        add(centerPanel, BorderLayout.CENTER);

        // Controls panel
        controlsPanel = new JPanel(new BorderLayout());

        // Direction buttons
        JPanel directionPanel = new JPanel(new GridLayout(2, 3));
        directionPanel.setBorder(BorderFactory.createTitledBorder("Movement"));

        JButton northBtn = new JButton("North");
        JButton southBtn = new JButton("South");
        JButton eastBtn = new JButton("East");
        JButton westBtn = new JButton("West");
        JButton lookBtn = new JButton("Look Around");
        JButton inventoryBtn = new JButton("Inventory");

        northBtn.addActionListener(e -> sendCommand("go north"));
        southBtn.addActionListener(e -> sendCommand("go south"));
        eastBtn.addActionListener(e -> sendCommand("go east"));
        westBtn.addActionListener(e -> sendCommand("go west"));
        lookBtn.addActionListener(e -> sendCommand("look"));
        inventoryBtn.addActionListener(e -> sendCommand("inventory"));
        
        directionPanel.add(new JLabel());
        directionPanel.add(northBtn);
        directionPanel.add(new JLabel());
        directionPanel.add(westBtn);
        directionPanel.add(lookBtn);
        directionPanel.add(eastBtn);
        directionPanel.add(new JLabel());
        directionPanel.add(southBtn);
        directionPanel.add(new JLabel());
        directionPanel.add(inventoryBtn);

        controlsPanel.add(directionPanel, BorderLayout.NORTH);

        // Command input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Commands"));

        commandInput = new JTextField();
        sendButton = new JButton("Send");

        commandInput.addActionListener(e -> processCommand());
        sendButton.addActionListener(e -> processCommand());

        inputPanel.add(commandInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        controlsPanel.add(inputPanel, BorderLayout.SOUTH);

        // Quick actions
        JPanel quickActions = new JPanel(new FlowLayout());
        quickActions.setBorder(BorderFactory.createTitledBorder("Quick Actions"));

        JButton takeBtn = new JButton("Take Item");
        JButton solveBtn = new JButton("Solve Puzzle");
        JButton resetBtn = new JButton("Reset Game");
        JButton rankingsBtn = new JButton("Rankings");

        takeBtn.addActionListener(e -> {
            Room currentRoom = gameManager.getCurrentRoom();
            java.util.List<Item> availableItems = currentRoom.getItems();
            
            if (availableItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items available in this room!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] itemNames = new String[availableItems.size()];
            for (int i = 0; i < availableItems.size(); i++) {
                itemNames[i] = availableItems.get(i).getName();
            }
            
            String selectedItem = (String) JOptionPane.showInputDialog(this,
                "Select item to take:",
                "Take Item",
                JOptionPane.QUESTION_MESSAGE,
                null,
                itemNames,
                itemNames[0]);
            
            if (selectedItem != null) {
                sendCommand("take " + selectedItem);
            }
        });

        solveBtn.addActionListener(e -> {
            String puzzleId = JOptionPane.showInputDialog(this, "Enter puzzle ID (riddle1 or combo1):");
            if (puzzleId != null && !puzzleId.trim().isEmpty()) {
                String solution = JOptionPane.showInputDialog(this, "Enter solution:");
                if (solution != null && !solution.trim().isEmpty()) {
                    sendCommand("solve " + puzzleId.trim() + " " + solution.trim());
                }
            }
        });

        resetBtn.addActionListener(e -> resetGame());
        rankingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Rankings feature coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        quickActions.add(takeBtn);
        quickActions.add(solveBtn);
        quickActions.add(resetBtn);
        quickActions.add(rankingsBtn);

        controlsPanel.add(quickActions, BorderLayout.CENTER);

        add(controlsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        addToLog(" WELCOME TO ESCAPE ROOM ADVENTURE!");
        addToLog("=================================");
        addToLog(" HOW TO PLAY:");
        addToLog("• Click 'Take Item' button to pick up items easily!");
        addToLog("• Solve puzzles to unlock rooms");
        addToLog("• Collect all 3 items to win!");
        addToLog(" WARNING: You have 5 minutes to escape!");
        addToLog("=================================");
    }

    private void startGameTimer() {
        gameTimer = new javax.swing.Timer(1000, e -> {
            if (gameActive) {
                timeRemaining--;
                updateTimerDisplay();
                
                if (timeRemaining <= 0) {
                    gameOver();
                }
            }
        });
        gameTimer.start();
    }

    private void updateTimerDisplay() {
        timerLabel.setText("Time: " + timeRemaining + "s");
        if (timeRemaining <= 60) {
            timerLabel.setForeground(Color.RED);
        } else if (timeRemaining <= 150) {
            timerLabel.setForeground(Color.ORANGE);
        }
    }

    private void processCommand() {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            commandInput.setText("");
            sendCommand(command);
        }
    }

    private void sendCommand(String command) {
        if (!gameActive) {
            addToLog("Game over! Reset to play again.");
            return;
        }

        addToLog("> " + command);

        try {
            switch (command.toLowerCase()) {
                case "look":
                    addToLog("📍 You look around carefully...");
                    updateGameDisplay();
                    break;
                case "inventory":
                    addToLog("📖 You check what you're carrying...");
                    updateInventoryDisplay();
                    break;
                default:
                    if (command.toLowerCase().startsWith("go ")) {
                        String direction = command.substring(3).trim();
                        handleMovement(direction);
                    } else if (command.toLowerCase().startsWith("take ")) {
                        String itemName = command.substring(5).trim();
                        handleItemPickup(itemName);
                    } else if (command.toLowerCase().startsWith("solve ")) {
                        String[] parts = command.substring(6).trim().split(" ", 2);
                        if (parts.length == 2) {
                            handlePuzzleSolving(parts[0], parts[1]);
                        } else {
                            addToLog("✕ Usage: solve [puzzleid] [answer]");
                        }
                    } else {
                        addToLog("✕ Unknown command.");
                    }
                    break;
            }
        } catch (Exception e) {
            addToLog("✕ Error: " + e.getMessage());
        }
    }

    private void handleMovement(String direction) {
        try {
            Room currentRoom = gameManager.getCurrentRoom();
            Room nextRoom = currentRoom.getConnection(direction);
            
            if (nextRoom == null) {
                addToLog("✕ You can't go that way!");
                return;
            }
            
            if (nextRoom instanceof LockedRoom) {
                LockedRoom lockedRoom = (LockedRoom) nextRoom;
                if (!lockedRoom.canEnter(gameManager)) {
                    showRiddlePrompt(lockedRoom.getRequiredPuzzleId());
                    return;
                }
            }
            
            gameManager.move(direction);
            addToLog("🎯 You move " + direction.toUpperCase() + " to " + gameManager.getCurrentRoom().getName());
            updateGameDisplay();
            checkWinCondition();
            
        } catch (Exception e) {
            addToLog("✕ Can't move that way: " + e.getMessage());
        }
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
                    if (puzzle.attemptSolve(solution.trim())) {
                        addToLog("🎉 Riddle solved! The door unlocks!");
                        JOptionPane.showMessageDialog(this,
                            "✅ CORRECT!\n\nThe door magically unlocks!",
                            "Riddle Solved",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        addToLog("❌ Wrong answer! Try again.");
                    }
                } catch (Exception e) {
                    addToLog("❌ " + e.getMessage());
                }
            }
        }
    }

    private Puzzle findPuzzleById(String puzzleId) {
        for (Room room : gameManager.getRooms().values()) {
            for (Puzzle puzzle : room.getPuzzles()) {
                if (puzzle.getId().equals(puzzleId)) {
                    return puzzle;
                }
            }
        }
        return null;
    }

    private void handleItemPickup(String itemName) {
        try {
            Room currentRoom = gameManager.getCurrentRoom();
            Item item = currentRoom.getItemByName(itemName);
            
            if (item == null) {
                addToLog("✕ No item found with name: " + itemName);
                return;
            }
            
            if (gameManager.pickUpItem(item)) {
                switch (item.getName().toLowerCase()) {
                    case "mysterious note":
                        addToLog("📜 You pick up the MYSTERIOUS NOTE");
                        addToLog("💡 The note says: 'The answer echoes in empty spaces...'");
                        break;
                    case "rusty key":
                        addToLog("🔑 You pick up the RUSTY KEY");
                        break;
                    case "golden torch":
                        addToLog("🔦 You pick up the GOLDEN TORCH");
                        break;
                    default:
                        addToLog("📦 You pick up: " + item.getName());
                }
                
                updateInventoryDisplay();
                updateGameDisplay();
                checkWinCondition();
            } else {
                addToLog("✕ Could not pick up " + itemName);
            }
            
        } catch (Exception e) {
            addToLog("✕ Error picking up item: " + e.getMessage());
        }
    }

    private void handlePuzzleSolving(String puzzleId, String solution) {
        try {
            Puzzle puzzle = findPuzzleById(puzzleId);
            if (puzzle == null) {
                addToLog("✕ No puzzle found with ID: " + puzzleId);
                return;
            }
            
            if (puzzle.attemptSolve(solution)) {
                addToLog("✅ CORRECT! Puzzle solved!");
                
                if (puzzleId.equals("riddle1")) {
                    addToLog("🚪 The library door is now unlocked!");
                } else if (puzzleId.equals("combo1")) {
                    addToLog("🔓 The safe opens revealing the Golden Torch!");
                }
            } else {
                addToLog("❌ Wrong answer! Try again.");
            }
            
            updateGameDisplay();
            checkWinCondition();
            
        } catch (Exception e) {
            addToLog("✕ Error solving puzzle: " + e.getMessage());
        }
    }

    private void updateGameDisplay() {
        Room currentRoom = gameManager.getCurrentRoom();

        StringBuilder roomInfo = new StringBuilder();
        roomInfo.append("📍 YOU ARE IN: ").append(currentRoom.getName()).append("\n\n");
        roomInfo.append("📝 DESCRIPTION:\n");
        roomInfo.append("  ").append(currentRoom.getDescription()).append("\n\n");

        java.util.List<Item> roomItems = currentRoom.getItems();
        if (!roomItems.isEmpty()) {
            roomInfo.append("📦 ITEMS HERE:\n");
            for (Item item : roomItems) {
                roomInfo.append("  • ").append(item.getName())
                       .append(" - ").append(item.getDescription()).append("\n");
            }
            roomInfo.append("\n");
        }

        java.util.List<Puzzle> puzzles = currentRoom.getPuzzles();
        if (!puzzles.isEmpty()) {
            roomInfo.append("❓ PUZZLES:\n");
            for (Puzzle puzzle : puzzles) {
                String status = puzzle.isSolved() ? "✅ SOLVED" : "❌ UNSOLVED";
                roomInfo.append("  • ").append(puzzle.getId()).append(": ").append(status).append("\n");
                if (!puzzle.isSolved()) {
                    roomInfo.append("     Q: ").append(puzzle.getQuestion()).append("\n");
                    roomInfo.append("     💡 Hint: ").append(puzzle.getHint()).append("\n");
                }
            }
            roomInfo.append("\n");
        }

        java.util.Map<String, Room> connections = currentRoom.getConnections();
        if (!connections.isEmpty()) {
            roomInfo.append("🚪 EXITS:\n");
            for (String exit : connections.keySet()) {
                Room connectedRoom = connections.get(exit);
                String lockStatus = (connectedRoom instanceof LockedRoom && !((LockedRoom) connectedRoom).canEnter(gameManager)) ? " 🔒" : "";
                roomInfo.append("  • ").append(exit.toUpperCase()).append(" → ")
                       .append(connectedRoom.getName()).append(lockStatus).append("\n");
            }
        }

        roomDisplay.setText(roomInfo.toString());
    }

    private void updateInventoryDisplay() {
        inventoryPanel.removeAll();

        java.util.List<Item> inventory = gameManager.getPlayerInventory();
        if (inventory.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your inventory is empty");
            emptyLabel.setForeground(Color.GRAY);
            inventoryPanel.add(emptyLabel);
        } else {
            for (Item item : inventory) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                JLabel itemLabel = new JLabel("• " + item.getName());
                itemLabel.setFont(new Font("Arial", Font.BOLD, 12));
                
                JLabel descLabel = new JLabel("  " + item.getDescription());
                descLabel.setForeground(Color.GRAY);
                descLabel.setFont(new Font("Arial", Font.ITALIC, 10));

                itemPanel.add(itemLabel, BorderLayout.NORTH);
                itemPanel.add(descLabel, BorderLayout.CENTER);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                inventoryPanel.add(itemPanel);
            }
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }

    private void addToLog(String message) {
        gameLog.append("• " + message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }

    private void resetGame() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reset the game?\nAll progress will be lost!",
            "Reset Game", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            gameTimer.stop();
            gameManager = new GameManager();
            initializeGame();
            timeRemaining = 300;
            gameActive = true;
            timerLabel.setForeground(Color.RED);
            startGameTimer();
            gameManager.startGame(gameManager.getRoom("room1"));
            gameLog.setText("");
            updateGameDisplay();
            updateInventoryDisplay();
            addToLog("🔄 GAME RESET!");
            addToLog("⏰ You have 5 minutes to escape!");
        }
    }

    private void checkWinCondition() {
        try {
            boolean allPuzzlesSolved = true;
            boolean allItemsCollected = gameManager.getPlayerInventory().size() >= 3;

            // Check if all puzzles are solved
            for (Room room : gameManager.getRooms().values()) {
                for (Puzzle puzzle : room.getPuzzles()) {
                    if (!puzzle.isSolved()) {
                        allPuzzlesSolved = false;
                        break;
                    }
                }
            }

            if (allPuzzlesSolved && allItemsCollected) {
                winGame();
            }
        } catch (Exception e) {
            // Ignore errors during win check
        }
    }

    private void winGame() {
        gameActive = false;
        gameTimer.stop();
        
        addToLog("");
        addToLog("🎉🎉🎉 CONGRATULATIONS! 🎉🎉🎉");
        addToLog("🏆 YOU ESCAPED THE ROOM!");
        addToLog("⭐ Player: " + currentPlayer);
        addToLog("⏰ Time remaining: " + timeRemaining + " seconds");
        addToLog("📊 Score: " + timeRemaining + " points");

        int choice = JOptionPane.showOptionDialog(this,
            "🎉 VICTORY! 🎉\n\n" +
            "Player: " + currentPlayer + "\n" +
            "Time Remaining: " + timeRemaining + " seconds\n" +
            "Score: " + timeRemaining + " points\n\n" +
            "You solved all puzzles and collected all items!",
            "🎊 VICTORY! 🎊",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Play Again", "Exit"},
            "Play Again");
        
        if (choice == 0) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new EscapeRoomGUI(currentPlayer));
        } else {
            System.exit(0);
        }
    }

    private void gameOver() {
        gameActive = false;
        gameTimer.stop();
        
        addToLog("");
        addToLog("💀💀💀 GAME OVER 💀💀💀");
        addToLog("⏰ Time ran out!");

        int choice = JOptionPane.showOptionDialog(this,
            "💀 TIME'S UP! GAME OVER! 💀\n\n" +
            "You didn't escape in time!\n\n" +
            "Try again and be faster next time!",
            "💀 GAME OVER 💀",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            new String[]{"Try Again", "Exit"},
            "Try Again");
        
        if (choice == 0) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new EscapeRoomGUI(currentPlayer));
        } else {
            System.exit(0);
        }
    }
}

// GAME CLASSES - ADD THESE AT THE END OF THE SAME FILE

class GameManager {
    private Map<String, Room> rooms = new HashMap<>();
    private Room currentRoom;
    private java.util.List<Item> playerInventory = new ArrayList<>();
    
    public static GameManager getInstance() {
        return new GameManager(); // Simplified - not using singleton pattern
    }
    
    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }
    
    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
    
    public Map<String, Room> getRooms() {
        return rooms;
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    public void startGame(Room startingRoom) {
        this.currentRoom = startingRoom;
    }
    
    public void move(String direction) {
        Room nextRoom = currentRoom.getConnection(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
        }
    }
    
    public java.util.List<Item> getPlayerInventory() {
        return playerInventory;
    }
    
    public boolean pickUpItem(Item item) {
        if (item != null && item.isCollectible()) {
            playerInventory.add(item);
            currentRoom.removeItem(item);
            return true;
        }
        return false;
    }
}

abstract class Room {
    protected String id;
    protected String name;
    protected String description;
    protected Map<String, Room> connections = new HashMap<>();
    protected java.util.List<Item> items = new ArrayList<>();
    protected java.util.List<Puzzle> puzzles = new ArrayList<>();
    
    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Map<String, Room> getConnections() { return connections; }
    public java.util.List<Item> getItems() { return items; }
    public java.util.List<Puzzle> getPuzzles() { return puzzles; }
    
    public void addConnection(String direction, Room room) {
        connections.put(direction, room);
    }
    
    public Room getConnection(String direction) {
        return connections.get(direction);
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    public void removeItem(Item item) {
        items.remove(item);
    }
    
    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
    
    public void addPuzzle(Puzzle puzzle) {
        puzzles.add(puzzle);
    }
    
    public abstract void interact();
}

class NormalRoom extends Room {
    public NormalRoom(String id, String name, String description) {
        super(id, name, description);
    }
    
    public void interact() {
        System.out.println("You are in " + name + ": " + description);
    }
}

class LockedRoom extends Room {
    private String requiredPuzzleId;
    
    public LockedRoom(String id, String name, String description, String requiredPuzzleId) {
        super(id, name, description);
        this.requiredPuzzleId = requiredPuzzleId;
    }
    
    public String getRequiredPuzzleId() {
        return requiredPuzzleId;
    }
    
    public boolean canEnter(GameManager gameManager) {
        // Check if the required puzzle is solved
        for (Room room : gameManager.getRooms().values()) {
            for (Puzzle puzzle : room.getPuzzles()) {
                if (puzzle.getId().equals(requiredPuzzleId)) {
                    return puzzle.isSolved();
                }
            }
        }
        return false;
    }
    
    public void interact() {
        System.out.println("You are in " + name + ": " + description);
    }
}

class Item {
    private String id;
    private String name;
    private String description;
    private boolean isCollectible;
    
    public Item(String id, String name, String description, boolean isCollectible) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isCollectible = isCollectible;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCollectible() { return isCollectible; }
}

abstract class Puzzle {
    private String id;
    private String question;
    private String answer;
    private String hint;
    private boolean solved;
    
    public Puzzle(String id, String question, String answer, String hint) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
        this.solved = false;
    }
    
    public String getId() { return id; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getHint() { return hint; }
    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
    
    public abstract boolean attemptSolve(String attempt);
}

class SimplePuzzle extends Puzzle {
    public SimplePuzzle(String id, String question, String answer, String hint) {
        super(id, question, answer, hint);
    }

    @Override
    public boolean attemptSolve(String attempt) {
        if (attempt.equalsIgnoreCase(getAnswer())) {
            setSolved(true);
            return true;
        }
        return false;
    }
}

class GameException extends Exception {
    public GameException(String message) {
        super(message);
    }
}