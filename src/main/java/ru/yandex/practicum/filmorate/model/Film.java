package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@NonNull
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film {

    private long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Duration duration;

    private Set<Long> likes;
}
