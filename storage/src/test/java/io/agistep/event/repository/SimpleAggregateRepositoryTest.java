package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleAggregateRepositoryTest {

    MapEventStorage eventStore;

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");
        eventStore = new MapEventStorage();
        assertThat(eventStore.getEventMap()).isEmpty();
    }

    @Test
    void xxx() {

        CommandProcessor<TodoAggregate, TodoCommand> commandProcessor = new GenericCommandProcessor<>(TodoCommand.class, eventStore);
        long createdAggregateId = commandProcessor.process(new CreateTodoCommand("title"));


        List<Event> events = eventStore.findByAggregate(createdAggregateId);
        assertThat(eventStore.getEventMap()).isNotEmpty();
        assertThat(events).hasSize(1);
    }

}