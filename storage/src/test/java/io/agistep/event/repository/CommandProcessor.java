package io.agistep.event.repository;

public interface CommandProcessor<COMMAND extends Command<? extends Aggregate>> {

    long process(COMMAND command);
}
