package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User owner;

    @BeforeEach
    void setUp() {
        owner = new User(10, "O", "o@o.com");
        item = new Item();
        item.setId(5);
        item.setName("Drill");
        item.setDescription("Desc");
        item.setIsAvailable(true);
        item.setOwner(owner);
    }

    @Test
    void editSuccess() {
        ItemDto dto = new ItemDto();
        dto.setName("New");
        dto.setDescription("NewDesc");
        dto.setAvailable(false);

        when(itemRepository.findById(5)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ItemDto res = itemService.edit(5, dto, 10);
        assertEquals("New", res.getName());
        assertEquals("NewDesc", res.getDescription());
        assertFalse(res.getAvailable());
    }

    @Test
    void editNotFound() {
        ItemDto dto = new ItemDto();
        when(itemRepository.findById(6)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.edit(6, dto, 10));
    }

    @Test
    void editForbidden() {
        Item other = new Item();
        other.setId(7);
        other.setOwner(new User(99, "X", "x@x.com"));
        when(itemRepository.findById(7)).thenReturn(Optional.of(other));
        ItemDto dto = new ItemDto();
        assertThrows(Exception.class, () -> itemService.edit(7, dto, 10));
    }

    @Test
    void getSuccess() {
        when(itemRepository.findById(5)).thenReturn(Optional.of(item));
        var res = itemService.get(5);
        assertEquals(5, res.getId());
    }

    @Test
    void getNotFound() {
        when(itemRepository.findById(8)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.get(8));
    }

    @Test
    void searchBlankReturnsEmpty() {
        var res = itemService.search("   ");
        assertTrue(res.isEmpty());
    }

    @Test
    void getAllReturnsList() {
        when(itemRepository.findByOwnerId(10)).thenReturn(List.of(item));
        var res = itemService.getAll(10);
        assertEquals(1, res.size());
    }

    @Test
    void addWithoutRequestSuccess() {
        ItemCreateDto create = new ItemCreateDto();
        create.setName("N");
        create.setDescription("D");
        create.setAvailable(true);

        when(userRepository.findById(10)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any())).thenAnswer(i -> {
            Item it = i.getArgument(0);
            it.setId(20);
            return it;
        });

        ItemDto res = itemService.add(create, 10);
        assertEquals(20, res.getId());
    }

    @Test
    void addWithRequestNotFound() {
        ItemCreateDto create = new ItemCreateDto();
        create.setRequestId(99);
        when(userRepository.findById(11)).thenReturn(Optional.of(new User(11, "U", "u@e.com")));
        when(itemRequestRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.add(create, 11));
    }

    @Test
    void addCommentSuccess() {
        CommentDto commentDto = new CommentDto(0, "", LocalDateTime.now(), "ok");

        User user = new User(3, "B", "b@b.com");
        Item it = new Item();
        it.setId(5);

        Booking booking = new Booking();
        booking.setItem(it);
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);

        when(userRepository.findById(3)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdAndStatus(3, Status.APPROVED)).thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenAnswer(i -> {
            Comment c = i.getArgument(0);
            c.setId(55);
            return c;
        });

        var res = itemService.addComment(commentDto, 3, 5);
        assertEquals(55, res.getId());
    }

    @Test
    void addCommentNoUser() {
        CommentDto commentDto = new CommentDto(0, "", LocalDateTime.now(), "");
        when(userRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.addComment(commentDto, 4, 5));
    }

    @Test
    void addCommentNoBooking() {
        CommentDto commentDto = new CommentDto(0, "", LocalDateTime.now(), "");
        User user = new User(6, "U", "u@e.com");
        when(userRepository.findById(6)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdAndStatus(6, Status.APPROVED)).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> itemService.addComment(commentDto, 6, 5));
    }

    @Test
    void addCommentBadRequestIfBookingFuture() {
        CommentDto commentDto = new CommentDto(0, "", LocalDateTime.now(), "");
        User user = new User(7, "U", "u@e.com");
        Item it = new Item(); it.setId(5);
        Booking booking = new Booking();
        booking.setItem(it);
        booking.setEnd(LocalDateTime.now().plusDays(1));

        when(userRepository.findById(7)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdAndStatus(7, Status.APPROVED)).thenReturn(List.of(booking));

        assertThrows(BadRequestException.class, () -> itemService.addComment(commentDto, 7, 5));
    }
}
