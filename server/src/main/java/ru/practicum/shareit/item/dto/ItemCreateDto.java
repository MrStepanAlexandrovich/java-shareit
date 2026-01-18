package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer owner;
    private Integer requestId;
    private List<CommentDto> comments;
}