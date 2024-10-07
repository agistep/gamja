package io.agistep.event.repository;

interface CommandProcessor<AGG extends Aggregate> {

    long process(Command<AGG> command);
}
