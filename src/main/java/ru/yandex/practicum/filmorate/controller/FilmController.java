package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        filmService.add(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.findAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void takeLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.unlikeFilm(filmId, userId);
    }
}
