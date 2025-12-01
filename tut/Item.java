public class Item {
    private String id;
    private String name;
    private String description;
    private boolean collectible;

    // MAIN METHOD FOR TESTING
    public static void main(String[] args) {
        Item testItem = new Item("test1", "Test Item", "A test item", true);
        System.out.println("Item created: " + testItem.getName());
    }

    public Item(String id, String name, String description, boolean collectible) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.collectible = collectible;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCollectible() { return collectible; }

    @Override
    public String toString() {
        return name;
    }
}