package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ItemClient itemClient;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setUp() {
        ItemController controller = new ItemController(itemClient);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    public void shouldAddItem() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setName("name");
        dto.setDescription("desc");
        dto.setAvailable(true);

        when(itemClient.add(anyLong(), any(ItemDto.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("created"));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"created\""));

        verify(itemClient, times(1)).add(eq(1L), any(ItemDto.class));
    }

    @Test
    public void shouldEditItem() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setName("n");
        dto.setDescription("d");
        dto.setAvailable(true);

        when(itemClient.edit(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(ResponseEntity.ok("edited"));

        MockMvc localMvc = MockMvcBuilders.standaloneSetup(new ItemController(itemClient))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        localMvc.perform(patch("/items/{itemId}", 5)
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"edited\""));

        verify(itemClient, times(1)).edit(eq(2L), eq(5L), any(ItemDto.class));
    }

    @Test
    public void shouldGetItemAndItemsAndSearch() throws Exception {
        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(ResponseEntity.ok("it"));
        when(itemClient.getItems(anyLong())).thenReturn(ResponseEntity.ok("list"));
        when(itemClient.search(anyLong(), anyString())).thenReturn(ResponseEntity.ok("found"));

        mockMvc.perform(get("/items/{itemId}", 7).header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"it\""));

        mockMvc.perform(get("/items").header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"list\""));

        mockMvc.perform(get("/items/search").param("text", "a").header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"found\""));

        verify(itemClient).getItem(eq(7L), eq(3L));
        verify(itemClient).getItems(eq(3L));
        verify(itemClient).search(eq(3L), eq("a"));
    }

    @Test
    public void shouldAddComment() throws Exception {
        CommentDto c = new CommentDto(1, "author", LocalDateTime.now(), "text");
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(ResponseEntity.ok("commented"));

        mockMvc.perform(post("/items/{itemId}/comment", 9)
                        .header("X-Sharer-User-Id", "4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"commented\""));

        verify(itemClient, times(1)).addComment(eq(9L), eq(4L), any(CommentDto.class));
    }
}
