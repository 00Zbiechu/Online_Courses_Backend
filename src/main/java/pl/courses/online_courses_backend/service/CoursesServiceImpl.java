package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.CoursesMapper;
import pl.courses.online_courses_backend.model.AddCoursesDTO;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.projection.CourseForList;
import pl.courses.online_courses_backend.projection.wrapper.CoursesForAdmin;
import pl.courses.online_courses_backend.repository.CoursesRepository;
import pl.courses.online_courses_backend.repository.UsersRepository;
import pl.courses.online_courses_backend.specification.CourseSpecification;
import pl.courses.online_courses_backend.specification.FoundCourses;
import pl.courses.online_courses_backend.specification.SearchCriteria;
import pl.courses.online_courses_backend.specification.SearchOperations;
import pl.courses.online_courses_backend.util.FileStorageUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CoursesServiceImpl extends AbstractService<CoursesEntity, CoursesDTO> implements CourseService {

    private final UsersRepository usersRepository;

    private final CoursesRepository coursesRepository;

    private final CoursesMapper coursesMapper;

    private final FileStorageUtil fileStorageUtil;

    private final CourseSpecification courseSpecification;


    @Override
    protected JpaRepository<CoursesEntity, Long> getRepository() {
        return coursesRepository;
    }

    @Override
    protected BaseMapper<CoursesEntity, CoursesDTO> getMapper() {
        return coursesMapper;
    }

    @Override
    public Long howManyCoursesIsInDatabase() {
        return coursesRepository.count();
    }

    @Override
    public Page<CourseForList> findCoursesPage(Pageable pageable) {

        return coursesRepository.findCoursesPage(pageable);

    }

    @Override
    public CoursesDTO addCourseWithRandomImageName(AddCoursesDTO addCoursesDTO) {
        addCoursesDTO.setImage(generateRandomImageName());

        UsersEntity usersEntity = usersRepository.findByUsername(addCoursesDTO.getUsername()).orElseThrow();

        CoursesDTO coursesDTO = CoursesMapper.INSTANCE.toCoursesDTOFromAddCoursesDTO(addCoursesDTO);

        CoursesEntity coursesEntity = CoursesMapper.INSTANCE.toEntity(coursesDTO);

        coursesEntity.setUsers((Set.of(usersEntity)));

        coursesRepository.save(coursesEntity);

        return coursesDTO;
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

    @Override
    public String uploadImageCourseImage(MultipartFile multipartFile) {

        try {
            fileStorageUtil.saveFile(multipartFile);
        } catch (IOException e) {
            return "Image save failed";
        }

        return "Image saved";

    }

    @Override
    public FoundCourses searchForCourses(String title, LocalDate startDate, LocalDate endDate, String topic) {

        CoursesDTO coursesDTO = CoursesDTO.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .topic(topic)
                .build();

        courseSpecification.clear();

        courseSpecification.add(new SearchCriteria("title", coursesDTO.getTitle(), SearchOperations.MATCH));
        courseSpecification.add(new SearchCriteria("startDate", coursesDTO.getStartDate(), SearchOperations.EQUAL));
        courseSpecification.add(new SearchCriteria("endDate", coursesDTO.getEndDate(), SearchOperations.EQUAL));
        courseSpecification.add(new SearchCriteria("topic", coursesDTO.getTopic(), SearchOperations.MATCH));

        List<CoursesDTO> list = coursesRepository.findAll(courseSpecification).stream()
                .map(coursesMapper::toDTO).toList();

        return FoundCourses.builder().foundCoursesList(list).build();

    }

    @Override
    public PageRequest buildPageRequestForCoursePage(Integer page, Integer size, String sort, String order) {

        if (order.equals("ASC")) {
            return PageRequest.of(page, size, Sort.by(sort).ascending());
        }

        return PageRequest.of(page, size, Sort.by(sort).descending());
    }

    @Override
    public CoursesForAdmin getCourseDataForAdmin(String username) {
        return CoursesForAdmin.builder()
                .courseForAdminList(coursesRepository.getCourseDataForAdmin(username))
                .build();
    }

}
