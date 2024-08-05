package ru.yandex.practicum.filmorate.storage.status;

import ru.yandex.practicum.filmorate.model.Status;

import java.util.List;

public interface StatusStorage {
    Status getStatusById(Long id);

    List<Status> getAllStatus();
}
