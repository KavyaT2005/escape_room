package puzzles;

import items.Item;
import exceptions.PuzzleSolveException;
import java.util.List;
import java.util.ArrayList;

public class ItemPuzzle extends Puzzle {
    private List<Item> requiredItems;
    private List<Item> providedItems;

    public ItemPuzzle(String id, String question, String answer, String hint) {
        super(id, question, answer, hint);
        this.requiredItems = new ArrayList<>();
        this.providedItems = new ArrayList<>();
    }

    public void addRequiredItem(Item item) {
        requiredItems.add(item);
    }

    public void provideItem(Item item) {
        providedItems.add(item);
    }

    @Override
    public boolean attemptSolve(String attempt) throws PuzzleSolveException {
        if (requiredItems.size() != providedItems.size()) {
            throw new PuzzleSolveException("Number of provided items does not match required items.");
        }

        for (Item required : requiredItems) {
            boolean found = false;
            for (Item provided : providedItems) {
                if (provided.getId().equals(required.getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new PuzzleSolveException("Missing required item: " + required.getName());
            }
        }

        setSolved(true);
        return true;
    }

    public void clearProvidedItems() {
        providedItems.clear();
    }

    public List<Item> getRequiredItems() {
        return requiredItems;
    }

    public List<Item> getProvidedItems() {
        return providedItems;
    }
}
