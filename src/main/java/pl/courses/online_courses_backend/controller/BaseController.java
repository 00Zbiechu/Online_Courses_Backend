package pl.courses.online_courses_backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.courses.online_courses_backend.service.BaseService;

import java.util.List;

public abstract class BaseController<D, S extends BaseService> {

    protected abstract S getService();

    @GetMapping
    public ResponseEntity<List<D>> get() {
        return new ResponseEntity<List<D>>(getService().get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody D dto) {
        return new ResponseEntity<>((D) getService().create(dto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<D> update(@PathVariable Long id, @Valid @RequestBody D dto) {
        return new ResponseEntity<>((D) getService().update(id, dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<D> delete(@Valid @RequestBody D dto) {
        return new ResponseEntity<>((D) getService().delete(dto), HttpStatus.ACCEPTED);
    }
}
