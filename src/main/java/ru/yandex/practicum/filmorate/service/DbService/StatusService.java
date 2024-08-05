package ru.yandex.practicum.filmorate.service.DbService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.storage.status.StatusStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class StatusService {
    public StatusStorage statusStorage;

    public Status getStatusById(Long id) {
        return statusStorage.getStatusById(id);
    }

    public List<Status> getAllStatus() {
        return statusStorage.getAllStatus();
    }
}
