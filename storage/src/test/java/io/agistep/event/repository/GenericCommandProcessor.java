package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.EventMaker;
import io.agistep.event.storages.MapEventStorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

class GenericCommandProcessor<AGG extends Aggregate> implements CommandProcessor<AGG> {

    private final MapEventStorage eventStore;
    private final String doProcess = "doProcess"; //TODO doProcess 를 어떻게 강제할 수 있지?

    public GenericCommandProcessor(MapEventStorage eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public long process(Command<AGG> command) {
        Class<?> commandClass = command.getClass();
        ParameterizedType commandInterface = (ParameterizedType) commandClass.getGenericInterfaces()[0];
        Class<AGG> aggClass = (Class<AGG>) commandInterface.getActualTypeArguments()[0];
        Method m = find(aggClass, command);
        if (m == null) {
            throw new RuntimeException(String.format("%s를 처리할 핸들러가 %s에 존재하지 않습니다.",
                    command.getClass().getSimpleName(), aggClass.getSimpleName()));
        }
        Object obj;
        try {
            obj = aggClass.getDeclaredConstructor().newInstance();
            Object invoke = m.invoke(obj, command);
            Event invoke1 = EventMaker.make(obj, invoke);
            eventStore.save(invoke1);
            return invoke1.getAggregateId();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // TODO (2024/10/27) :  1. 실제 클래스를 가져오고 메서드(Command)가 있는지 확인하고 실행시켜서 이벤트를 발행하게 하기
        // TODO (2024/10/27) :  2. update에 대해 처리
        // TODO (2024/10/27) :  3. eventStore가 드러나있는 테스트케이스를 어떻게 할것인가..
    }

    @Override
    public long process(long aggregateId, Command<AGG> command) {
        Class<?> commandClass = command.getClass();
        ParameterizedType commandInterface = (ParameterizedType) commandClass.getGenericInterfaces()[0];
        Class<AGG> aggClass = (Class<AGG>) commandInterface.getActualTypeArguments()[0];
        Method m = find(aggClass, command);

        if (m == null) {
            throw new RuntimeException(String.format("%s를 처리할 핸들러가 %s에 존재하지 않습니다.",
                    command.getClass().getSimpleName(), aggClass.getSimpleName()));
        }

        try {
            AGG obj = aggClass.getDeclaredConstructor().newInstance();

            List<Event> byAggregate = eventStore.findByAggregate(aggregateId); //TODO if events in DB
            Object invoke = m.invoke(obj, command);
            Event event = EventMaker.make(obj, invoke);
            byAggregate.add(event);

            eventStore.save(byAggregate);
            return event.getAggregateId();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method find(Class<AGG> aggClass, Command<AGG> command) {
        Method[] methods = aggClass.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> doProcess.equals(method.getName()) && method.getParameterTypes()[0].equals(command.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found valid Command method"));
    }
}
