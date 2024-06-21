package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String login;

    private String name;

    private LocalDate birthday;

}