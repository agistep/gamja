package io.agistep.event.repository;

interface CommandProcessor<AGG extends Aggregate, COMMAND extends Command<AGG>> {

    long process(COMMAND command);
}
