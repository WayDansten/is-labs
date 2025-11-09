package app.event;

import java.util.Set;

import app.websocket.WebSocketMessageType;

public class DataChangedEvent {
    private final Set<WebSocketMessageType> messageTypes;
    
    public DataChangedEvent(Set<WebSocketMessageType> messageTypes) {
        this.messageTypes = messageTypes;
    }
    
    public Set<WebSocketMessageType> getMessageTypes() {
        return messageTypes;
    }
}