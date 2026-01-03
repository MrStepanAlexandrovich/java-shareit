package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookingDto {
    private Integer bookingId;
    private UserDto user;
    private ItemDto item;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status approved;
}
