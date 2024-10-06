package io.agistep.event.repository;

import io.agistep.aggregator.AggregateId;
import io.agistep.event.Event;
import io.agistep.event.EventHandler;

class TodoAggregate implements Aggregate {

    @AggregateId
    long id;
    String title;

    public long getId() {
        return id;
    }
}
