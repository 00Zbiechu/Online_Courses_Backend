package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.CoursesMapper;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.other.FileStorageUtil;
import pl.courses.online_courses_backend.repository.CoursesRepository;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CoursesService extends AbstractService<CoursesEntity, CoursesDTO> {


    private final CoursesRepository coursesRepository;

    private final CoursesMapper coursesMapper;

    private final FileStorageUtil fileStorageUtil;


    @Override
    protected JpaRepository<CoursesEntity, Long> getRepository() {
        return coursesRepository;
    }

    @Override
    protected BaseMapper<CoursesEntity, CoursesDTO> getMapper() {
        return coursesMapper;
    }

    public Long howManyCoursesIsInDatabase() {
        return coursesRepository.count();
    }

    public Page<CoursesDTO> findCoursesPage(Pageable pageable) {

        List<CoursesDTO> list = coursesRepository.findCoursesPage(pageable).stream()
                .map(coursesMapper::toDTO)
                .toList();

        return new PageImpl<>(list);

    }

    public CoursesDTO addCourseWithRandomImageName(CoursesDTO coursesDTO) {
        coursesDTO.setImage(generateRandomImageName());
        return create(coursesDTO);
    }

    private String generateRandomImageName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString + ".jpg";
    }

    public String uploadImageCourseImage(MultipartFile multipartFile) {

        try {
            fileStorageUtil.saveFile(multipartFile);
        } catch (IOException e) {
            return "Image save failed";
        }

        return "Image saved";

    }


}
