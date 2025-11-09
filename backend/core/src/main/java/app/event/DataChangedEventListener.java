package app.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import app.websocket.WebSocketNotifier;

@ApplicationScoped
@NoArgsConstructor
public class DataChangedEventListener {
    @Transactional
    public void onDataChanged(@Observes(during = TransactionPhase.AFTER_SUCCESS) DataChangedEvent event) {
        event.getMessageTypes().forEach(WebSocketNotifier::broadcast);
    }
}
