package pl.courses.online_courses_backend.mapper;

public interface BaseMapper<E, D> {

    E toEntity(D dto);

    D toDTO(E entity);
}
