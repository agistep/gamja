package io.agistep.event.storages;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import io.agistep.event.EventSource;
import io.agistep.foo.Foo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapEventStorageTest {

    MapEventStorage sut;

    @Test
    void name() {
        Foo foo = new Foo();
        Object payload = new Object();
        EventSource.apply(foo, payload);


    }


}