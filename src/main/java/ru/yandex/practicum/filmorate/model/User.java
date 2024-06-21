package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private Long id;

    @NonNull
    @EqualsAndHashCode.Include
    private String email;

    @NonNull
    @EqualsAndHashCode.Include
    private String login;

    private String name;

    private LocalDate birthday;

}