package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;
import io.agistep.foo.Foo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class SimpleAggregateRepositoryTest {

    MapEventStorage eventStore;

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");
        eventStore = new MapEventStorage();
        assertThat(eventStore.getEventMap()).isEmpty();
    }

    @Test
    @Disabled
    void xxx() {

        CommandProcessor<TodoAggregate> commandProcessor = new GenericCommandProcessor<>(eventStore);

        long createdAggregateId = commandProcessor.process(new CreateTodoCommand("title"));


        List<Event> events = eventStore.findByAggregate(createdAggregateId);
        assertThat(eventStore.getEventMap()).isNotEmpty();
        assertThat(events).hasSize(1);
    }

    @Test
    void xxxx() {
        CommandProcessor<Foo> commandProcessor = new GenericCommandProcessor<>(eventStore);

        assertThatThrownBy(() -> commandProcessor.process(new CreateFooCommand()))
                .hasMessageContaining("CreateFooCommand를 처리할 핸들러가 Foo에 존재하지 않습니다.");
    }

    @Test
    void xxxx2() {
        Event event = mock();
        CommandProcessor<Foo> commandProcessor = new GenericCommandProcessor<>(eventStore);

        long id = commandProcessor.process(new CreateFooCommand());

        List<Event> events = eventStore.findByAggregate(id);
        assertThat(events.get(0).getAggregateId()).isEqualTo(id);
        assertThat(events.get(0).getPayload()).isNotNull();
        assertThat(events.get(0).getName()).isEqualTo(events.get(0).getPayload().getClass().getName());
    }

    static class Foo implements Aggregate {


    }

    static class CreateFooCommand implements Command<Foo> {
    }


}
