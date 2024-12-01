package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import io.agistep.foo.Foo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MapEventStorageTest {

    MapEventStorage sut;

    @BeforeEach
    void setUp() {
        sut = new MapEventStorage();
    }

    @Test
    void saveTest() {
        List<Event> events = new ArrayList<>();
        Event make = EventMaker.make(new Foo(), EventMaker.payload("TEST"));
        events.add(make);

        sut.save(events);

        List<Event> byAggregate = sut.findByAggregate(make.getAggregateId());
        assertThat(byAggregate).contains(make);
    }
}