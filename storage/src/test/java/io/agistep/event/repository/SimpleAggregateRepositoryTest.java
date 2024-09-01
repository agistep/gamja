package io.agistep.event.repository;

import io.agistep.aggregator.AggregateId;
import io.agistep.event.*;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimpleAggregateRepositoryTest {

    @BeforeEach
    void setUp() {
        System.setProperty("basePackage", "io.agistep");
    }

    @Test
    void xxx() {
        MapEventStorage eventStore = new MapEventStorage();
        AggregateRepository<TodoAggregate> aggregateRepository = new AggregateRepository<>(TodoAggregate.class, eventStore);

        CommandProcessor commandProcessor = new CommandProcessor(TodoCommand.class, aggregateRepository);
        TodoTitle todoTitle = new TodoTitle();

        commandProcessor.addListener(todoTitle);
        long createdAggregateId = commandProcessor.process(new CreateTodoCommand("title2"));
        assertThat(todoTitle.getTitle()).isEqualTo("title2");
        assertThat(createdAggregateId).isGreaterThan(0);

//        commandProcessor.process(createdAggregateId, new UpdateTodoCommand("title3"));
//        assertThat(todoTitle.getTitle()).isEqualTo("title3");

    }

    private static class TodoTitle {
        String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private static class AggregateRepository<T> {

        private final Class<T> aggregate;
        private final MapEventStorage eventStorage;

        public AggregateRepository(Class<T> aggregate, MapEventStorage eventStorage) {
            this.aggregate = aggregate;
            this.eventStorage = eventStorage;
        }

        public long save(CreateTodoCommand command) {
            String title = command.getTitle();
            TodoAggregate aggregate = new TodoAggregate();
            TodoCreatedEvent event = new TodoCreatedEvent(title);
            Event makedEvent = EventMaker.make(aggregate, event);
            eventStorage.save(makedEvent);
            EventSource.apply2(aggregate, makedEvent);
            return makedEvent.getAggregateId();
        }

    }
    private static class TodoCreatedEvent {
        private final String title;

        public TodoCreatedEvent(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private static class TodoAggregate {

        @AggregateId
        long id;

        @EventHandler(payload = TodoCreatedEvent.class)
        public void eventHandler(Event event){
            long aggregateId = event.getAggregateId();
            System.out.println("Hello world>> " + aggregateId);
            this.id = aggregateId;
        }

        public long getId() {
            return id;
        }
    }

    private static class CommandProcessor {

        private final AggregateRepository<TodoAggregate> aggregateRepository;
        private TodoTitle todoTitle;

        public CommandProcessor(Class<TodoCommand> todoCommandClass, AggregateRepository<TodoAggregate> aggregateRepository) {
            this.aggregateRepository = aggregateRepository;
        }

        public void addListener(TodoTitle todoTitle) {
            this.todoTitle = todoTitle;
        }

        public long process(CreateTodoCommand command) {
            long aggregateId = aggregateRepository.save(command);
            List<Event> events = ThreadLocalEventHolder.instance().getEventAll();
            for (Event event : events) {
                Object payload = event.getPayload();
                if (payload instanceof TodoCreatedEvent todoCreatedEvent) {
                    String title = todoCreatedEvent.getTitle();
                    todoTitle.setTitle(title);
                }
            }
            return aggregateId;
        }

        public void process(long createdAggregateId, UpdateTodoCommand title2) {
        }
    }

    private interface TodoCommand {
    }

    private static class CreateTodoCommand implements TodoCommand {
        private final String title;

        public CreateTodoCommand(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private class UpdateTodoCommand {
        public UpdateTodoCommand(String title2) {
        }
    }
}