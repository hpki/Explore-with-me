package ru.practicum.explorewithme.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private int hits;
}
