package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("UPDATE Booking b SET b.status=:status WHERE b.id=:id")
    @Modifying
    void updateStatus(
            @Param("id") int id,
            @Param("status") Booking.Status status
    );

    @Query("SELECT b FROM Booking b JOIN FETCH b.booker JOIN FETCH b.item WHERE b.id=:id")
    Optional<Booking> findBooking(@Param("id") int id);

    Collection<Booking> findByBookerIdAndStatus(int userId, Booking.Status status);
}
