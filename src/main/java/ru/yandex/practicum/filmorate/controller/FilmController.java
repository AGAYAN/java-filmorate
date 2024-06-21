package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> filmMap = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28", formatter))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() == null || film.getDuration().getSeconds() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }

        Long nextId = getFilmId();
        film.setId(nextId);
        filmMap.put(nextId, film);

        log.info("Фильм добавлен: {}", film.getName());
        return film;
    }

    private Long getFilmId() {
        OptionalLong maxIdOpt = filmMap.keySet().stream().mapToLong(Long::longValue).max();
        return maxIdOpt.orElse(0L) + 1;
    }

    @GetMapping
    public Map<Long, Film> getFilms() {
        return filmMap;
    }
}
