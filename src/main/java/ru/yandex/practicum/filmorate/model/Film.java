package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {

    private long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    private LocalDate releaseDate;

    private Duration duration;
}