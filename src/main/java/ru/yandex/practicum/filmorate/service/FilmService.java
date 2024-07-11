package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService implements FilmServiceInterface {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        Film existingFilm = filmStorage.findById(film.getId());
        if (existingFilm == null) {
            log.error("Фильм с идентефикатором : {}", film.getId() + " не найден");
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        validate(film);
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilmByID(Long filmId) {
        checkContains(filmId);
        return filmStorage.findById(filmId);
    }

    @Override
    public void likeFilm(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> likesByFilm = film.getLikes();
        if (likesByFilm.contains(userId)) {
            log.error("Этим юзером лайк уже поставлен");
            throw new IllegalArgumentException("Невозможно поставить лайк фильму с id: " + id + " повторно");
        }
        log.info("Фильму с id: {} поставлен лайк от юзера с id: {}", id, userId);
        likesByFilm.add(userId);
    }


    @Override
    public void unlikeFilm(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        if (film == null) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> likesByFilm = film.getLikes();
        if (!likesByFilm.contains(userId)) {
            log.error("Ошибка при удалении лайка у фильма с id: {} от юзера с id: {}", id, userId);
            throw new NotFoundException("Вы не ставили лайк фильму с id: " + id);
        }
        log.info("У фильма с id: {} отозван лайк от юзера с id: {}", id, userId);
        likesByFilm.remove(userId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.findAll().stream()
                .filter(n -> n.getLikes() != null)
                .sorted((n, f) -> f.getLikes().size() - n.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkContains(Long filmId) {

        if (filmStorage.findById(filmId) == null) {
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28", DateTimeFormatter.ISO_DATE))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration().getSeconds() <= 0) {
            log.error("Продолжительность не может быть отрицательной или равной нулю");
            throw new ValidationException("Продолжительность не может быть отрицательной или равной нулю");
        }
    }
}
