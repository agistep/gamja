package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;

import java.time.LocalDateTime;

class TodoCommandProcessor implements CommandProcessor<TodoCommand> {

    private final MapEventStorage eventStore;

    public TodoCommandProcessor(Class<TodoCommand> todoCommandClass, MapEventStorage eventStore) {
        // todo >
        this.eventStore = eventStore;
    }

    @Override
    public long process(TodoCommand command) {
        Event event = new Event() {
            @Override
            public long getId() {
                return 0;
            }

            @Override
            public long getSeq() {
                return 0;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public long getAggregateId() {
                return 0;
            }

            @Override
            public Object getPayload() {
                return null;
            }

            @Override
            public LocalDateTime getOccurredAt() {
                return null;
            }
        };
        eventStore.save(event);
        return event.getAggregateId();
    }
}
