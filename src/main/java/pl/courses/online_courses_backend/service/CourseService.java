package pl.courses.online_courses_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.model.FileDataDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.ParticipantsDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;

public interface CourseService extends BaseService<CourseDTO> {

    Page<CourseWithAuthorDTO> findCoursesPage(PaginationForCourseListDTO paginationForCourseListDTO);

    CourseDTO addCourse(AddCourseDTO addCourseDTO);

    CoursesDTO deleteCourse(Long courseId);

    PhotoDTO getCourseImage(Long courseId);

    PhotoDTO uploadCourseImage(Long courseId, MultipartFile multipartFile);

    PhotoDTO deleteCourseImage(Long courseId);

    CoursesDTO getCourseDataForUser();

    CourseDTO editCourse(EditCourseDTO editCourseDTO);

    CourseWithAuthorDTO getCourse(Long courseId, String password);

    TopicsDTO addTopic(Long courseId, MultipartFile[] files, AddTopicDTO addTopicDTO);

    TopicsDTO getTopics(Long courseId, String password);

    TopicsDTO deleteTopic(Long courseId, Long topicId);

    FileDataDTO getAttachment(Long courseId, Long topicId, Long fileId, String password);

    ParticipantsDTO getCourseParticipants(Long courseId);
}
