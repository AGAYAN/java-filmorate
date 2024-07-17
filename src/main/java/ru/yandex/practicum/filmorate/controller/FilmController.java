package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
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
    public Film addFilm(@RequestBody Film film) {
        return filmService.add(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable Long id) {
        return filmService.getFilmByID(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilm(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void takeLike(@PathVariable("id") Long filmId, @PathVariable Long userId) { // void
        filmService.likeFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) { // void
        filmService.unlikeFilm(filmId, userId);
    }
}
