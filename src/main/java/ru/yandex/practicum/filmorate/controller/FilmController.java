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

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if ((LocalDate.parse("15.02.2016", DateTimeFormatter.ofPattern("dd.MM.yyyy")).isBefore(film.getReleaseDate()))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration().getSeconds() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }

        Long nextId = getFilmId();
        film.setId(nextId);
        filmMap.put(nextId, film);

        return film;
    }

    @GetMapping
    public Map<Long, Film> getFilms() {
        log.info("Всего фильмов: {}", filmMap.values().size());
        return filmMap;
    }

    private Long getFilmId() {
        OptionalLong maxIdOpt = filmMap.keySet().stream().mapToLong(Long::longValue).max();
        return maxIdOpt.orElse(0L) + 1;
    }
}
