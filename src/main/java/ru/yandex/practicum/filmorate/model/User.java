package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private Long id;

    @NotBlank(message = "Логин не может быть пустым и не должен содержать пробелы")
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не может содержать пробелы")
    @Size(max = 20, message = "Логин должен содержать не более 20 символов")
    private String login;

    private String name;

    @NotEmpty(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна быть корректной")
    private String email;

    @NotNull(message = "Дата рождения не может быть null")
    @PastOrPresent(message = "Дата рождения должна быть в прошлом или настоящем")
    private LocalDate birthday;

    private Status friend;
}