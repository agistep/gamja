package io.agistep.event.repository;

import io.agistep.aggregator.AggregateId;
import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
    void findAggregateClassTest() {
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
        assertThat(events.get(0).getName()).isEqualTo(FooCreatedEvent.class.getName());
    }

    @Test
    void name() {
        CommandProcessor<Foo> commandProcessor = new GenericCommandProcessor<>(eventStore);
        long id = commandProcessor.process(new CreateFooCommand());

        commandProcessor.process(id, new EditFooCommand());

        List<Event> events = eventStore.findByAggregate(id);
        // TODO 다음 seq 알 수 없어 OptimisticLockingException 발생
        assertThat(events.get(0).getName()).isEqualTo(FooCreatedEvent.class.getName());
        assertThat(events.get(1).getName()).isEqualTo(FooEditedEvent.class.getName());
    }
    //TODO 테스트 케이스를 보고 앞으로 무엇을 할지를 고민

    static class Foo implements Aggregate {

        @AggregateId
        long id;

        FooCreatedEvent doProcess(CreateFooCommand command) {
            return new FooCreatedEvent();
        }

        FooEditedEvent doProcess(EditFooCommand command) {
            return new FooEditedEvent();
        }



    }

    public static final class FooCreatedEvent {
        public FooCreatedEvent() {
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj != null && obj.getClass() == this.getClass();
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return "FooCreatedEvent[]";
        }
    }

    static class CreateFooCommand implements Command<Foo> {
    }


    static class EditFooCommand implements Command<Foo> {
    }

    public static final class FooEditedEvent {
    }
}
