package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ItemWithBookingsDto extends ItemDto {
    private LocalDate lastBooking;
    private LocalDate nextBooking;
}
