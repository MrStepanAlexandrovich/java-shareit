package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserEditDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserClient userClient;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        UserController controller = new UserController(userClient);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(validator)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void shouldGetUser() throws Exception {
        when(userClient.getUser(anyLong())).thenReturn(ResponseEntity.ok("ok"));

        mockMvc.perform(get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("\"ok\""));

        verify(userClient, times(1)).getUser(eq(1L));
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        when(userClient.createUser(any(UserCreateDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("created"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"created\""));

        verify(userClient, times(1)).createUser(any(UserCreateDto.class));
    }

    @Test
    void shouldEditUser() throws Exception {
        UserEditDto dto = new UserEditDto();
        dto.setName("Jane");
        dto.setEmail("jane@example.com");

        when(userClient.editUser(anyLong(), any(UserEditDto.class)))
                .thenReturn(ResponseEntity.ok("edited"));

        mockMvc.perform(patch("/users/{userId}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"edited\""));

        verify(userClient, times(1)).editUser(eq(2L), any(UserEditDto.class));
    }

    @Test
    void shouldRemoveUser() throws Exception {
        doNothing().when(userClient).removeUser(anyLong());

        mockMvc.perform(delete("/users/{userId}", 3))
                .andExpect(status().isOk());

        verify(userClient, times(1)).removeUser(eq(3L));
    }
}
