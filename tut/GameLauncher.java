import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Check if all required classes exist before creating the launcher
            try {
                Class.forName("EscapeRoomGUI");
                Class.forName("RankingManager");
                new GameLauncherFrame();
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, 
                    "Missing required classes!\nMake sure all game files are compiled.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class GameLauncherFrame extends JFrame {
    private JTextField usernameField;
    private JButton startButton;
    private JButton rankingsButton;
    private JButton exitButton;
    
    public GameLauncherFrame() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Escape Room Adventure - Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        // Header
        JLabel titleLabel = new JLabel("🚪 ESCAPE ROOM ADVENTURE 🚪", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Username input
        JLabel userLabel = new JLabel("Enter Your Name:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        
        // Buttons
        startButton = new JButton("🎮 START GAME");
        rankingsButton = new JButton("🏆 VIEW RANKINGS");
        exitButton = new JButton("❌ EXIT");
        
        styleButton(startButton, new Color(34, 139, 34));
        styleButton(rankingsButton, new Color(255, 140, 0));
        styleButton(exitButton, new Color(220, 20, 60));
        
        // Add components to main panel
        mainPanel.add(userLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(startButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(rankingsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(exitButton);
        
        // Add action listeners
        startButton.addActionListener(e -> startGame());
        rankingsButton.addActionListener(e -> showRankings());
        exitButton.addActionListener(e -> System.exit(0));
        
        // Add to frame
        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Instructions
        JTextArea instructions = new JTextArea(
            "🎯 HOW TO PLAY:\n" +
            "• Explore rooms and solve puzzles\n" +
            "• Collect all 3 items\n" +
            "• Solve riddles to progress\n" +
            "• Escape within 5 minutes!\n\n" +
            "🎯 GAME FLOW:\n" +
            "1. Find Mysterious Note in Entrance\n" +
            "2. Solve Riddle in Library\n" +
            "3. Open Safe in Treasure Room\n" +
            "4. Collect all items and ESCAPE!"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(240, 240, 240));
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void styleButton(JButton button, Color color) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 45));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    private void startGame() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your name to start the game!", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if EscapeRoomGUI exists before trying to create it
        try {
            Class.forName("EscapeRoomGUI");
            this.dispose();
            // Use reflection to create instance
            SwingUtilities.invokeLater(() -> {
                try {
                    java.lang.reflect.Constructor<?> constructor = 
                        Class.forName("EscapeRoomGUI").getConstructor(String.class);
                    constructor.newInstance(username);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Cannot start game: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "EscapeRoomGUI class not found!\nCompile all game files first.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRankings() {
        // Check if RankingManager exists
        try {
            Class.forName("RankingManager");
            java.lang.reflect.Method method = 
                Class.forName("RankingManager").getMethod("displayRankings");
            method.invoke(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "RankingManager not available yet!", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}