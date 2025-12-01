package interfaces;

import patterns.GameEvent;

public interface GameObserver {
    void update(GameEvent event);
}