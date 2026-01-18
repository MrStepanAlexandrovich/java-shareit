package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusTest {

    @Test
    void testStatusValues() {
        Status[] values = Status.values();

        assertThat(values).hasSize(4);
        assertThat(values).contains(Status.WAITING, Status.REJECTED, Status.APPROVED, Status.CANCELED);
    }

    @Test
    void testStatusValueOf() {
        assertThat(Status.valueOf("WAITING")).isEqualTo(Status.WAITING);
        assertThat(Status.valueOf("REJECTED")).isEqualTo(Status.REJECTED);
        assertThat(Status.valueOf("APPROVED")).isEqualTo(Status.APPROVED);
        assertThat(Status.valueOf("CANCELED")).isEqualTo(Status.CANCELED);
    }

    @Test
    void testStatusName() {
        assertThat(Status.WAITING.name()).isEqualTo("WAITING");
        assertThat(Status.REJECTED.name()).isEqualTo("REJECTED");
        assertThat(Status.APPROVED.name()).isEqualTo("APPROVED");
        assertThat(Status.CANCELED.name()).isEqualTo("CANCELED");
    }

    @Test
    void testStatusOrdinal() {
        assertThat(Status.WAITING.ordinal()).isEqualTo(0);
        assertThat(Status.REJECTED.ordinal()).isEqualTo(1);
        assertThat(Status.APPROVED.ordinal()).isEqualTo(2);
        assertThat(Status.CANCELED.ordinal()).isEqualTo(3);
    }
}
