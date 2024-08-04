package io.agistep.event.repository;

import io.agistep.aggregator.AggregateId;
import io.agistep.event.Event;
import io.agistep.event.EventHandler;
import io.agistep.event.EventSource;
import io.agistep.event.ThreadLocalEventHolder;
import io.agistep.event.storages.MapEventStorage;
import io.agistep.foo.FooCreated;
import org.assertj.core.api.Assertions;
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
        long createdAggregateId = commandProcessor.process(new CreateTodoCommand("title"));
        assertThat(todoTitle.getTitle()).isEqualTo("title");
        assertThat(createdAggregateId).isGreaterThan(0);

//        commandProcessor.process(createdAggregateId, new UpdateTodoCommand("title2"));
//        assertThat(todoTitle.getTitle()).isEqualTo("title2");

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

        public AggregateRepository(Class<T> aggregate, MapEventStorage p1) {
        }

        public long save(CreateTodoCommand command) {
            TodoAggregate aggregate = new TodoAggregate();
            EventSource.apply(aggregate, new TodoCreatedEvent());
            return 1;
        }


    }
    private static class TodoCreatedEvent {
    }

    private static class TodoAggregate {

        @AggregateId
        long id;

        @EventHandler(payload = TodoCreatedEvent.class)
        public void eventHandler(TodoCreatedEvent event){

        }
    }

    private static class CommandProcessor {

        private final AggregateRepository<TodoAggregate> aggregateRepository;
        private TodoTitle todoTitle;

        public CommandProcessor(Class<TodoCommand> todoCommandClass, AggregateRepository<TodoAggregate> aggregateRepository) {
            this.aggregateRepository = aggregateRepository;
        }

        public long process(CreateTodoCommand command) {
            long save = aggregateRepository.save(command);
            List<Event> events = ThreadLocalEventHolder.instance().getEventAll();
            for (Event event : events) {
                todoTitle.setTitle("title");
            }


            return save;
        }

        public void addListener(TodoTitle todoTitle) {
            this.todoTitle = todoTitle;
        }
    }

    private interface TodoCommand {
    }

    private static class CreateTodoCommand implements TodoCommand {
        public CreateTodoCommand(String title) {

        }
    }

    private class UpdateTodoCommand {
        public UpdateTodoCommand(String title2) {
        }
    }
}