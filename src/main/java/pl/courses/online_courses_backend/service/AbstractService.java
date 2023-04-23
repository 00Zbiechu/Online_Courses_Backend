package pl.courses.online_courses_backend.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.courses.online_courses_backend.entity.BaseEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractService<E extends BaseEntity, D> implements BaseService<D>{

    protected abstract JpaRepository<E, Long> getRepository();

    protected abstract BaseMapper<E, D> getMapper();

    public List<D> get() {
        return getRepository().findAll().stream().map(getMapper()::toDTO).collect(Collectors.toList());
    }

    public D create(D dto) {
        getRepository().save(getMapper().toEntity(dto));
        return dto;
    }

    public D update(Long id, D dto) {
        getRepository().deleteById(id);
        getRepository().save(getMapper().toEntity(dto));
        return dto;
    }

    public D delete(D dto) {
        getRepository().delete(getMapper().toEntity(dto));
        return dto;
    }

}
