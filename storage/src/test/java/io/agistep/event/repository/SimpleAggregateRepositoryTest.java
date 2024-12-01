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
    void findAggregateClassTest() {
        CommandProcessor<FooAggregate> commandProcessor = new GenericCommandProcessor<>(eventStore);

        assertThatThrownBy(() -> commandProcessor.process(new NoneEventFooCommand()))
                .hasMessageContaining("NoneEventFooCommand를 처리할 핸들러가 FooAggregate에 존재하지 않습니다.");
    }

    @Test
    void apply_CreateEvent_CommandProcessorTest() {
        CommandProcessor<FooAggregate> commandProcessor = new GenericCommandProcessor<>(eventStore);

        long id = commandProcessor.process(new CreateFooCommand());

        List<Event> events = eventStore.findByAggregate(id);
        assertThat(events.get(0).getName()).isEqualTo(FooCreatedEvent.class.getName());
    }

    @Test
    void apply_UpdateEvent_CommandProcessorTest() {
        CommandProcessor<FooAggregate> commandProcessor = new GenericCommandProcessor<>(eventStore);
        long id = commandProcessor.process(new CreateFooCommand());

        commandProcessor.process(id, new EditFooCommand());

        List<Event> events = eventStore.findByAggregate(id);
        // TODO 다음 seq 알 수 없어 OptimisticLockingException 발생
        assertThat(events.get(0).getName()).isEqualTo(FooCreatedEvent.class.getName());
        assertThat(events.get(1).getName()).isEqualTo(FooEditedEvent.class.getName());
    }
    //TODO 테스트 케이스를 보고 앞으로 무엇을 할지를 고민

    static class FooAggregate implements Aggregate {

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
    }

    static class CreateFooCommand implements Command<FooAggregate> {
    }


    static class EditFooCommand implements Command<FooAggregate> {
    }

    static class NoneEventFooCommand implements Command<FooAggregate> {
    }

    public static final class FooEditedEvent {
    }
}
