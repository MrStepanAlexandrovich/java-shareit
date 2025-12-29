package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ItemWithBookingsDto extends ItemDto {
    private LocalDate lastBooking;
    private LocalDate nextBooking;
}
