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
import pl.courses.online_courses_backend.model.*;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesForUserDTO;
import pl.courses.online_courses_backend.photo.PhotoCompressor;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;
import pl.courses.online_courses_backend.type.OrderType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursesServiceImpl extends AbstractService<CourseEntity, CourseDTO> implements CourseService {

    private final CurrentUser currentUser;

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final PhotoCompressor photoCompressor;

    @Override
    protected JpaRepository<CourseEntity, Long> getRepository() {
        return courseRepository;
    }

    @Override
    protected BaseMapper<CourseEntity, CourseDTO> getMapper() {
        return courseMapper;
    }

    @Override
    public Page<CourseForListDTO> findCoursesPage(Pageable pageable) {
        Page<CourseEntity> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::toCourseForList);
    }

    @Override
    public CoursesDTO addCourse(AddCourseDTO addCourseDTO, MultipartFile photo) {
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
        courseRepository.save(courseEntity);

        if (photo != null && !photo.isEmpty()) {
            uploadCourseImage(courseEntity.getId(), photo);
        }

        return findAllCoursesOfUser();
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
    public CoursesForUserDTO getCourseDataForUser() {
        List<CourseEntity> list = courseRepository.findCoursesCreatedByUser(currentUser.getCurrentlyLoggedUser().getId());
        List<CourseForUserDTO> resultList = list.stream().map(courseMapper::toCourseForAdmin).toList();
        return CoursesForUserDTO.builder().courseForUserDTOList(resultList).build();
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
}
