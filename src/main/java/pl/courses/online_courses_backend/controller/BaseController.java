package pl.courses.online_courses_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.service.AbstractService;

import java.util.List;

public abstract class BaseController<D, S extends AbstractService> {

    protected abstract S getService();

    @GetMapping
    List<D> get() {
        return getService().get();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody D dto) {
        getService().create(dto);
        return ResponseEntity.ok("Zapisano");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody D dto) {
        getService().update(id, dto);
        return ResponseEntity.ok("update wykonany");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody D dto) {
        getService().delete(dto);
        return ResponseEntity.ok("Usunięto");
    }
}
