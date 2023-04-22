package pl.courses.online_courses_backend.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.courses.online_courses_backend.entity.BaseEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractService<E extends BaseEntity, D> {

    protected abstract JpaRepository<E, Long> getRepository();

    protected abstract BaseMapper<E, D> getMapper();

    public List<D> get() {
        return getRepository().findAll().stream().map(getMapper()::toDTO).collect(Collectors.toList());
    }

    public void create(D dto) {
        getRepository().save(getMapper().toEntity(dto));
    }

    public void update(Long id, D dto) {
        getRepository().deleteById(id);
        getRepository().save(getMapper().toEntity(dto));
    }

    public void delete(D dto) {
        getRepository().delete(getMapper().toEntity(dto));
    }

}
