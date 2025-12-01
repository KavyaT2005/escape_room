project structure:- 
EscapeRoomGame/
├── EscapeRoomGUI.java          # Main game interface
├── GameManager.java            # Game logic controller
├── RankingManager.java         # Score tracking system
├── Room.java                   # Room base class
├── NormalRoom.java            # Standard room implementation
├── LockedRoom.java            # Puzzle-locked room
├── Item.java                  # Item/object class
├── Puzzle.java                # Puzzle base class
├── SimplePuzzle.java          # Basic puzzle implementation
├── CombinationPuzzle.java     # Combination lock puzzle
├── PuzzleSolveException.java  # Custom exception
└── scores.txt                 # Score data file (auto-generated)

# Compile all Java files
javac *.java
# Run the game
java EscapeRoomGUI
