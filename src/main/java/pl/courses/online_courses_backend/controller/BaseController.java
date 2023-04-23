package pl.courses.online_courses_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.service.AbstractService;

import java.util.List;

public abstract class BaseController<D, S extends AbstractService> {

    protected abstract S getService();

    @GetMapping
    public ResponseEntity<List<D>> get() {
        return new ResponseEntity<List<D>>(getService().get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody D dto) {
        return new ResponseEntity<D>((D) getService().create(dto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<D> update(@PathVariable Long id, @RequestBody D dto) {
        return new ResponseEntity<D>((D) getService().update(id, dto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<D> delete(@RequestBody D dto) {
        return new ResponseEntity<D>((D) getService().delete(dto), HttpStatus.ACCEPTED);
    }
}
