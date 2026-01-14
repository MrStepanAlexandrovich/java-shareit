package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ItemRequestClient itemRequestClient;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        ItemRequestController controller = new ItemRequestController(itemRequestClient);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldGetUsersRequests() throws Exception {
        when(itemRequestClient.getUsersRequests(anyInt())).thenReturn(ResponseEntity.ok("ok"));

        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"ok\""));

        verify(itemRequestClient, times(1)).getUsersRequests(eq(1));
    }

    @Test
    void shouldAddRequest() throws Exception {
        ItemRequestCreateDto dto = new ItemRequestCreateDto();
        dto.setDescription("d");

        when(itemRequestClient.addRequest(anyInt(), any(ItemRequestCreateDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("created"));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"created\""));

        verify(itemRequestClient, times(1)).addRequest(eq(2), any(ItemRequestCreateDto.class));
    }

    @Test
    void shouldGetAllOtherRequestsAndGetRequest() throws Exception {
        when(itemRequestClient.getOthersRequests(anyInt())).thenReturn(ResponseEntity.ok("others"));
        when(itemRequestClient.getRequest(anyInt())).thenReturn(ResponseEntity.ok("req"));

        mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"others\""));

        mockMvc.perform(get("/requests/{requestId}", 5))
                .andExpect(status().isOk())
                .andExpect(content().string("\"req\""));

        verify(itemRequestClient).getOthersRequests(eq(3));
        verify(itemRequestClient).getRequest(eq(5));
    }
}

