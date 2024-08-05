package ru.yandex.practicum.filmorate.dao.rating;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RatingDbStorageTest {
    private final RatingDbStorage ratingDbStorage;

    @Test
    public void getAllRating() {
        List<Mpa> mpas = ratingDbStorage.getAllRating();
        Assertions.assertEquals(5, mpas.size());
    }
}