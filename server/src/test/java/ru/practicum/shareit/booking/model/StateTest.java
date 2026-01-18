package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StateTest {

    @Test
    void testStateValues() {
        State[] values = State.values();

        assertThat(values).hasSize(6);
        assertThat(values).contains(State.ALL, State.CURRENT, State.PAST, State.FUTURE, State.WAITING, State.REJECTED);
    }

    @Test
    void testStateValueOf() {
        assertThat(State.valueOf("ALL")).isEqualTo(State.ALL);
        assertThat(State.valueOf("CURRENT")).isEqualTo(State.CURRENT);
        assertThat(State.valueOf("PAST")).isEqualTo(State.PAST);
        assertThat(State.valueOf("FUTURE")).isEqualTo(State.FUTURE);
        assertThat(State.valueOf("WAITING")).isEqualTo(State.WAITING);
        assertThat(State.valueOf("REJECTED")).isEqualTo(State.REJECTED);
    }

    @Test
    void testStateName() {
        assertThat(State.ALL.name()).isEqualTo("ALL");
        assertThat(State.CURRENT.name()).isEqualTo("CURRENT");
        assertThat(State.PAST.name()).isEqualTo("PAST");
        assertThat(State.FUTURE.name()).isEqualTo("FUTURE");
        assertThat(State.WAITING.name()).isEqualTo("WAITING");
        assertThat(State.REJECTED.name()).isEqualTo("REJECTED");
    }

    @Test
    void testStateOrdinal() {
        assertThat(State.ALL.ordinal()).isEqualTo(0);
        assertThat(State.CURRENT.ordinal()).isEqualTo(1);
        assertThat(State.PAST.ordinal()).isEqualTo(2);
        assertThat(State.FUTURE.ordinal()).isEqualTo(3);
        assertThat(State.WAITING.ordinal()).isEqualTo(4);
        assertThat(State.REJECTED.ordinal()).isEqualTo(5);
    }
}
