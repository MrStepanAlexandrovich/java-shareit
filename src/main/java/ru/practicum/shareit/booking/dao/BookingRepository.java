package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("UPDATE Booking b SET b.status=:status WHERE b.id=:id")
    @Modifying(clearAutomatically = true)
    void updateStatus(
            @Param("id") int id,
            @Param("status") Booking.Status status
    );

    @Query("SELECT b FROM Booking b JOIN FETCH b.booker JOIN FETCH b.item WHERE b.id=:id")
    Optional<Booking> findBooking(@Param("id") int id);

    Collection<Booking> findByBookerIdAndStatus(int userId, Booking.Status status);

    Collection<Booking> findByBookerId(int userId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE b.status=:status and b.item.owner.id=:userId")
    Collection<Booking> getUsersItemsThatBooked(@Param("status") int userId, @Param("status") Booking.Status status);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE b.end <= :now " +
            "and b.item.id=:itemId ORDER BY b.end DESC LIMIT 1")
    Optional<Booking> findLastBooking(@Param("now")LocalDateTime now,
                                      @Param("itemId")int itemId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE b.start >= :now " +
            "and b.item.id=:itemId ORDER BY b.start ASC LIMIT 1")
    Optional<Booking> findNextBooking(@Param("now")LocalDateTime now,
                                      @Param("itemId")int itemId);
}
