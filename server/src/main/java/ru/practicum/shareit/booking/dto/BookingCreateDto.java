package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingCreateDto {
    private Integer userId;
    private Integer itemId;
    private LocalDateTime start;

    @Future
    private LocalDateTime end;
}
