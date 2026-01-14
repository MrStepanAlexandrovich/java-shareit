package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEditDto {
    private int id;
    private String name;
    private String email;

}
