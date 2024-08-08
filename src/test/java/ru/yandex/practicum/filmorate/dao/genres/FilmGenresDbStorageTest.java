package ru.yandex.practicum.filmorate.dao.genres;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenresDbStorageTest {
    private final GenreDbStorage filmStorage;

    @Test
    public void getGenreById() {
        final Optional<Genre> genreOptional = filmStorage.getGenresById(1L);

        assertThat(genreOptional).isPresent();
        assertThat(genreOptional.get()).hasFieldOrPropertyWithValue("id", 1L);

    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = filmStorage.getAllGenres();
        System.out.println("Returned genres: " + genres);
        assertEquals(6, genres.size(), "List length must be 6");
    }

}