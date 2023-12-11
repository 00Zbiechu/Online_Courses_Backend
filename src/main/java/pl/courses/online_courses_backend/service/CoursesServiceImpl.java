package pl.courses.online_courses_backend.service;

import com.google.common.collect.Sets;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.authentication.CurrentUser;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.CourseMapper;
import pl.courses.online_courses_backend.mapper.TopicMapper;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;
import pl.courses.online_courses_backend.photo.PhotoCompressor;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.validator.TopicValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursesServiceImpl extends AbstractService<CourseEntity, CourseDTO> implements CourseService {

    private final CurrentUser currentUser;

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final TopicMapper topicMapper;

    private final TopicValidator topicValidator;

    private final PhotoCompressor photoCompressor;

    private final PasswordEncoder passwordEncoder;

    @Override
    protected JpaRepository<CourseEntity, Long> getRepository() {
        return courseRepository;
    }

    @Override
    protected BaseMapper<CourseEntity, CourseDTO> getMapper() {
        return courseMapper;
    }

    @Override
    public Page<CourseWithAuthorDTO> findCoursesPage(Pageable pageable) {
        Page<CourseEntity> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::toCourseForList);
    }

    @Override
    public CourseDTO addCourse(AddCourseDTO addCourseDTO) {
        UserEntity currentUserEntity = currentUser.getCurrentlyLoggedUser();
        CourseEntity courseEntity = courseMapper.toEntity(addCourseDTO);

        CourseUsersEntity courseUserEntity = CourseUsersEntity.builder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(currentUserEntity)
                        .build())
                .owner(Boolean.TRUE)
                .build();

        courseEntity.setCourseUser(Sets.newHashSet(courseUserEntity));
        var result = courseRepository.save(courseEntity);

        return courseMapper.toDTO(result);
    }

    @Override
    public CourseDTO editCourse(EditCourseDTO editCourseDTO) {
        var courseEntity = findCourseOfUser(editCourseDTO.getId());
        courseMapper.updateCourseEntityFromEditCourseDTO(editCourseDTO, courseEntity);
        courseEntity.setPassword(passwordEncoder.encode(courseEntity.getPassword()));
        var result = courseRepository.save(courseEntity);
        return courseMapper.toDTO(result);
    }

    @Override
    public CourseWithAuthorDTO getCourse(Long courseId) {
        var courseEntity = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        return courseMapper.toCourseForList(courseEntity);
    }

    @Override
    public CoursesDTO deleteCourse(Long courseId) {
        var courseEntity = findCourseOfUser(courseId);
        courseRepository.delete(courseEntity);
        return findAllCoursesOfUser();
    }

    @Override
    public PhotoDTO getCourseImage(Long courseId) {
        var courseEntity = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        return PhotoDTO.builder().photo(courseEntity.getPhoto()).build();
    }

    @Override
    public PhotoDTO uploadCourseImage(Long courseId, MultipartFile photo) {

        var courseEntity = findCourseOfUser(courseId);

        Try.run(() -> {
            var compressedPhoto = photoCompressor.resizeImage(photo, 400, 400);
            courseEntity.setPhoto(compressedPhoto);
            courseRepository.save(courseEntity);
        }).onFailure(image -> {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FIELD_FORMAT, HttpStatus.BAD_REQUEST);
        });
        return PhotoDTO.builder().photo(courseEntity.getPhoto()).build();
    }

    @Override
    public PhotoDTO deleteCourseImage(Long courseId) {
        var courseEntity = findCourseOfUser(courseId);
        courseEntity.setPhoto(null);
        var result = courseRepository.save(courseEntity);
        return PhotoDTO.builder().photo(result.getPhoto()).build();
    }

    @Override
    public PageRequest buildPageRequestForCoursePage(PaginationForCourseListDTO paginationForCourseListDTO) {
        Sort.Direction direction = (paginationForCourseListDTO.getOrder() == OrderType.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = paginationForCourseListDTO.getSort().getFieldName();

        return PageRequest.of(
                paginationForCourseListDTO.getPage(),
                paginationForCourseListDTO.getSize(),
                Sort.by(direction, sortField)
        );
    }

    @Override
    public CoursesDTO getCourseDataForUser() {
        List<CourseEntity> list = courseRepository.findCoursesCreatedByUser(currentUser.getCurrentlyLoggedUser().getId());
        var coursesForUser = list.stream().map(courseMapper::toDTO).toList();
        return CoursesDTO.builder().foundCoursesList(coursesForUser).build();
    }

    private CourseEntity findCourseOfUser(Long courseId) {
        List<CourseEntity> coursesCreatedByUser = courseRepository
                .findCoursesCreatedByUser(currentUser.getCurrentlyLoggedUser().getId());

        return coursesCreatedByUser.stream()
                .filter(course -> courseId.equals(course.getId()))
                .findFirst()
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    private CoursesDTO findAllCoursesOfUser() {
        List<CourseEntity> list = courseRepository.findCoursesCreatedByUser(currentUser.getCurrentlyLoggedUser().getId());
        List<CourseDTO> resultList = list.stream().map(courseMapper::toDTO).toList();
        return CoursesDTO.builder().foundCoursesList(resultList).build();
    }

    @Override
    public TopicsDTO addTopic(Long courseId, MultipartFile[] files, TopicDTO topicDTO) {

        var courseEntity = findCourseOfUser(courseId);

        topicValidator.validateIsTopicUnique(topicDTO, courseEntity);

        var topicEntity = topicMapper.toEntity(topicDTO);
        topicEntity.setCourseEntity(courseEntity);

        if (courseEntity.getTopics() == null) {
            courseEntity.setTopics(Sets.newHashSet(topicEntity));
        } else {
            courseEntity.getTopics().add(topicEntity);
        }

        var result = courseRepository.save(courseEntity);

        var topicList = result.getTopics().stream().map(topicMapper::toDTO).toList();
        return TopicsDTO.builder().topics(topicList).build();
    }
}
