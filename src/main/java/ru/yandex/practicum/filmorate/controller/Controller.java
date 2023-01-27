package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidationException.NOT_FOUND;

@Slf4j
public abstract class Controller<T extends Requestable> {
	protected final Map<Integer, T> objects = new HashMap<>();
	protected int generatorID = 1;

	protected abstract void validate(T obj) throws ValidationException;

	private void logException(HttpStatus status, Exception exception) {
		log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
	}

	private void logVariablesCondition() {
        log.info(LOG_SIZE_FILMS.message, objects.size());
		log.debug(LOG_ID_GEN.message, generatorID);
	}

	private void isExist(T obj) throws ValidationException {
		Integer id = obj.getId();
		if (id == null) throw new ValidationException(ID_NOT_IS_BLANK);
		if (objects.get(id) == null) throw new ValidationException(NOT_FOUND);
	}

	@GetMapping
	public List<Requestable> getAllObjects() {
		return new ArrayList<>(objects.values());
	}

	@PostMapping
	public ResponseEntity<Requestable> create(@Valid @RequestBody T obj) {
		try {
			validate(obj);
			obj.setId(generatorID++);
			objects.put(obj.getId(), obj);
			return ResponseEntity.ok(obj);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
		}
	}

	@PutMapping
	public ResponseEntity<Requestable> update(@Valid @RequestBody T obj) {
		try {
			validate(obj);
			isExist(obj);
			objects.put(obj.getId(), obj);
			return ResponseEntity.ok(obj);
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(obj);
		}
	}
}