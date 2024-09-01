package io.agistep.event.repository;

import io.agistep.aggregator.AggregateId;
import io.agistep.event.*;
import io.agistep.event.storages.MapEventStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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
//        assertThat(ThreadLocalEventHolder.instance().getEventAll()).hasSize(1);

        commandProcessor.process(createdAggregateId, new UpdateTodoCommand("title3"));
        assertThat(todoTitle.getTitle()).isEqualTo("title3");
//        assertThat(ThreadLocalEventHolder.instance().getEventAll()).hasSize(2);

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

        public void update(long aggregateId, UpdateTodoCommand command) {
            TodoAggregate todoAggregate = new TodoAggregate();
            List<Event> byAggregate = eventStorage.findByAggregate(aggregateId);
            EventSource.replay(todoAggregate, byAggregate.toArray(new Event[]{}));;

            TodoUpdateEvent todoUpdateEvent = new TodoUpdateEvent(command.getTitle());
            Event makedEvent = EventMaker.make(todoAggregate, todoUpdateEvent);
            eventStorage.save(makedEvent);
            EventSource.apply2(todoAggregate, makedEvent);
        }


        public List<Event> findEventsByAggregateId(long aggregateId) {
            return eventStorage.findByAggregate(aggregateId);
        }
    }

    private static class TodoUpdateEvent {
        private final String title;

        public TodoUpdateEvent(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
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
        String title;

        @EventHandler(payload = TodoCreatedEvent.class)
        public void eventHandler(Event event){
            TodoCreatedEvent payload = (TodoCreatedEvent) event.getPayload();
            this.id = event.getAggregateId();
            this.title = payload.getTitle();
        }

        @EventHandler(payload = TodoUpdateEvent.class)
        public void eventHandler(TodoUpdateEvent event){
            this.title = event.getTitle();
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

            List<Event> events = aggregateRepository.findEventsByAggregateId(aggregateId);
//            List<Event> events = ThreadLocalEventHolder.instance().getEventAll();
            for (Event event : events) {
                Object payload = event.getPayload();
                if (payload instanceof TodoCreatedEvent todoCreatedEvent) {
                    String title = todoCreatedEvent.getTitle();
                    todoTitle.setTitle(title);
                }
            }
            return aggregateId;
        }

        public void process(long aggregateId, UpdateTodoCommand command) {
            aggregateRepository.update(aggregateId, command);

//            List<Event> events = ThreadLocalEventHolder.instance().getEventAll(); //TODO aggregate 를 어떻게 테스트 할 수 있는가?
            List<Event> events = aggregateRepository.findEventsByAggregateId(aggregateId);
            for (Event event : events) {
                Object payload = event.getPayload();

                if (payload instanceof TodoCreatedEvent todoCreatedEvent) {
                    String title = todoCreatedEvent.getTitle();
                    todoTitle.setTitle(title);
                }

                if (payload instanceof TodoUpdateEvent todoUpdateEvent) {
                    String title = todoUpdateEvent.getTitle();
                    todoTitle.setTitle(title);
                }
            }
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

        private final String title;

        public UpdateTodoCommand(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}