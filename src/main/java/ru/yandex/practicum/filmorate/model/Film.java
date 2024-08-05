package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Valid
public class Film {

    private long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 100, message = "Имя должно содержать не более 100 символов")
    private String name;

    @Size(max = 200, message = "Описание должно содержать не более 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть null")
    private LocalDate releaseDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Positive(message = "Продолжительность должна быть положительной")
    private Long duration;

    private Mpa mpa;

    private Set<Long> likes;

    private Set<Genre> genres = new HashSet<>();

    public void addGenre(Genre genre) {
        if (this.genres == null) {
            this.genres = new HashSet<>();
        }
        this.genres.add(genre);
    }
}