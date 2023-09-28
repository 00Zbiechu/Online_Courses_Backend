package pl.courses.online_courses_backend.service;

import java.util.List;

public interface BaseService<D> {

    List<D> get();

    D create(D dto);

    D update(Long id, D dto);

    D delete(D dto);
}
