package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("UPDATE Booking b SET b.status=:status WHERE b.id=:id")
    @Modifying(clearAutomatically = true)
    void updateStatus(
            @Param("id") int id,
            @Param("status") Status status
    );

    @Query("SELECT b FROM Booking b JOIN FETCH b.booker JOIN FETCH b.item WHERE b.id=:id")
    Optional<Booking> findBooking(@Param("id") int id);

    Collection<Booking> findByBookerIdAndStatus(int userId, Status status);

    Collection<Booking> findByBookerIdAndStatus(int userId, Status status, Sort sort);

    Collection<Booking> findByBookerId(int userId, Sort sort);

    Collection<Booking> findByBookerIdAndEndIsBefore(int userId, LocalDateTime dateTime, Sort sort);

    Collection<Booking> findByBookerIdAndStartIsAfter(int userId, LocalDateTime dateTime, Sort sort);

    Collection<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(int userId, LocalDateTime dateTime1,
                                                                LocalDateTime dateTime2, Sort sort);

    Collection<Booking> findByItemOwnerId(int userId, Sort sort);

    Collection<Booking> findByItemOwnerIdAndEndIsBefore(int userId, LocalDateTime now, Sort sort);

    Collection<Booking> findByItemOwnerIdAndStartIsAfter(int userId, LocalDateTime now, Sort sort);

    Collection<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(int userId, LocalDateTime dateTime1,
                                                                       LocalDateTime dateTime2, Sort sort);

    Collection<Booking> findByItemOwnerIdAndStatus(int userId, Status status, Sort sort);
}
