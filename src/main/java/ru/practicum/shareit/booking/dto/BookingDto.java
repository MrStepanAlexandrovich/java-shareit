package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookingDto {
    private Integer bookingId;
    private Integer userId;
    private Integer itemId;
    private LocalDateTime start;
    private Boolean isApproved;
}
