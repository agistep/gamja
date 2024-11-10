package io.agistep.event.repository;

interface CommandProcessor<AGG extends Aggregate> {

    /**
     *
     * @param command
     * @return id affected by command
     */
    long process(Command<AGG> command);

    long process(long id, Command<AGG> command);
}
