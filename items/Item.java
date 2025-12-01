package items;

import interfaces.Interactive;

public class Item implements Interactive {
    private String id;
    private String name;
    private String description;
    private boolean collectible;
    private boolean usable; // Add this field

    public Item(String id, String name, String description, boolean collectible) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectible = collectible;
        this.usable = true; // Default to usable
    }

    // Add this constructor if you need to specify usability
    public Item(String id, String name, String description, boolean collectible, boolean usable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectible = collectible;
        this.usable = usable;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCollectible() { return collectible; }
    
    // ✅ Add this missing method
    public boolean isUsable() {
        return usable;
    }

    @Override
    public void interact() {
        System.out.println("Item: " + name + " - " + description);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}