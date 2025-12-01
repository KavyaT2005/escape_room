public class GameEvent {
    public enum Type {
        GAME_STARTED,
        GAME_ENDED,
        ROOM_CHANGED,
        ITEM_COLLECTED,
        ITEM_USED,
        PUZZLE_SOLVED,
        GAME_WON,
        GAME_LOST
    }

    private String message;
    private Type type;

    public GameEvent(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }
}