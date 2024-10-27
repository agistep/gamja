package io.agistep.event.repository;

import io.agistep.event.Event;
import io.agistep.event.storages.MapEventStorage;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;

class GenericCommandProcessor<AGG extends Aggregate> implements CommandProcessor<AGG> {

    private final MapEventStorage eventStore;

    public GenericCommandProcessor(MapEventStorage eventStore) {
        // todo >
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
        // TODO (2024/10/27) :  1. 실제 클래스를 가져오고 메서드가 있는지 확인하고 실행시켜서 이벤트를 발행하게 하기
        // TODO (2024/10/27) :  2. update에 대해 처리
        // TODO (2024/10/27) :  3. eventStore가 드러나있는 테스트케이스를 어떻게 할것인가..

        Event event = new Event() {
            @Override
            public long getId() {
                return 0;
            }

            @Override
            public long getSeq() {
                return 0;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public long getAggregateId() {
                return 0;
            }

            @Override
            public Object getPayload() {
                return null;
            }

            @Override
            public LocalDateTime getOccurredAt() {
                return null;
            }
        };
        eventStore.save(event);
        return event.getAggregateId();
    }

    public long process2(Command<AGG> command, Class clz) {
        Class<AGG> aggClass = getAggregateClassBy2(command, clz);
        Method m = find(aggClass, command);
        if (m == null) {
            throw new RuntimeException(String.format("%s를 처리할 핸들러가 %s에 존재하지 않습니다.",
                    command.getClass().getSimpleName(), aggClass.getSimpleName()));
        }
        // TODO (2024/10/27) :  1. 실제 클래스를 가져오고 메서드가 있는지 확인하고 실행시켜서 이벤트를 발행하게 하기
        // TODO (2024/10/27) :  2. update에 대해 처리
        // TODO (2024/10/27) :  3. eventStore가 드러나있는 테스트케이스를 어떻게 할것인가..

        Event event = new Event() {
            @Override
            public long getId() {
                return 0;
            }

            @Override
            public long getSeq() {
                return 0;
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public long getAggregateId() {
                return 0;
            }

            @Override
            public Object getPayload() {
                return null;
            }

            @Override
            public LocalDateTime getOccurredAt() {
                return null;
            }
        };
        eventStore.save(event);
        return event.getAggregateId();
    }

    private Class<AGG> getAggregateClassBy2(Command<AGG> command, Class clz, AGG ... agg) {
        return (getClassOf(agg));
    }

    private static <T> Class<T> getClassOf(T[] array) {
        Class<? extends Object[]> aClass = array.getClass();
        Class<?> componentType = aClass.getComponentType();
        //noinspection unchecked
        return (Class<T>) componentType;
    }


    private Method find(Class<AGG> aggClass, Command<AGG> command) {
        return null;
    }

    @SafeVarargs
    private Class<AGG> getAggregateClassBy(Command<AGG> command, AGG ... agg) {
        return getClassOf(agg);
    }
//
//    private static <T> Class<T> getClassOf(T[] array) {
//        return (Class<T>) array.getClass().getComponentType();
//    }

}
