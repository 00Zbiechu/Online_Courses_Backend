package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.CoursesMapper;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.repository.CoursesRepository;

@Service
@RequiredArgsConstructor
public class CoursesService extends AbstractService<CoursesEntity, CoursesDTO> {


    private final CoursesRepository coursesRepository;

    private final CoursesMapper coursesMapper;

    @Override
    protected JpaRepository<CoursesEntity, Long> getRepository() {
        return coursesRepository;
    }

    @Override
    protected BaseMapper<CoursesEntity, CoursesDTO> getMapper() {
        return coursesMapper;
    }


}
