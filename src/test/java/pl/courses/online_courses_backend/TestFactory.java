package pl.courses.online_courses_backend;

import com.google.common.collect.Sets;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.entity.ConfirmationTokenEntity;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;
import pl.courses.online_courses_backend.model.UserDTO;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.type.SortType;

import java.time.LocalDate;

public class TestFactory {

    public static class CourseEntityFactory {

        public static CourseEntity.CourseEntityBuilder createCourseEntityBuilder() {
            return CourseEntity.builder();
        }

        public static CourseEntity createCourseEntity() {
            return createCourseEntityBuilder()
                    .title("Course title")
                    .topic("Course topic")
                    .description("Course description")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .password("CoursePassword")
                    .topics(Sets.newHashSet())
                    .build();
        }
    }

    public static class UserEntityFactory {

        public static UserEntity.UserEntityBuilder createUserEntityBuilder() {
            return UserEntity.builder();
        }

        public static UserEntity createUserEntity() {
            return createUserEntityBuilder()
                    .username("TestUsername")
                    .email("Test@java.com.pl")
                    .password("UserPassword")
                    .tokens(Sets.newHashSet())
                    .role(Role.USER)
                    .deleted(false)
                    .enabled(true)
                    .courseUser(Sets.newHashSet())
                    .build();
        }
    }

    public static class CourseUsersEntityFactory {

        public static CourseUsersEntity.CourseUsersEntityBuilder createCourseUsersEntityBuilder() {
            return CourseUsersEntity.builder();
        }
    }

    public static class PaginationForCourseListDTOFactory {

        public static PaginationForCourseListDTO.PaginationForCourseListDTOBuilder createPaginationForCourseListDTOBuilder() {
            return PaginationForCourseListDTO.builder();
        }

        public static PaginationForCourseListDTO createPaginationForCourseListDTO() {
            return createPaginationForCourseListDTOBuilder()
                    .page(0)
                    .order(OrderType.ASC)
                    .sort(SortType.TITLE)
                    .size(2)
                    .build();
        }
    }

    public static class SearchForCourseDTOFactory {
        public static SearchForCourseDTO.SearchForCourseDTOBuilder createSearchForCourseDTOBuilder() {
            return SearchForCourseDTO.builder();
        }
    }

    public static class AddCourseDTOFactory {

        public static AddCourseDTO.AddCourseDTOBuilder createAddCourseDTOBuilder() {
            return AddCourseDTO.builder();
        }

        public static AddCourseDTO createAddCourseDTO() {
            return createAddCourseDTOBuilder()
                    .title("Course")
                    .topic("Course topic")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .description("Course description")
                    .password("Course_password")
                    .build();
        }
    }

    public static class EditCourseDTOFactory {

        public static EditCourseDTO.EditCourseDTOBuilder createEditCourseDTOBuilder() {
            return EditCourseDTO.builder();
        }

        public static EditCourseDTO createEditCourseEdit(Long courseId) {
            return (EditCourseDTO) createEditCourseDTOBuilder()
                    .id(courseId)
                    .title("Course")
                    .topic("Course topic")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .description("Course description")
                    .password("Course_password")
                    .build();
        }
    }

    public static class UserDTOFactory {
        public static UserDTO.UserDTOBuilder createUserDTOBuilder() {
            return UserDTO.builder();
        }

        public static UserDTO createUserDTO() {
            return createUserDTOBuilder()
                    .username("UserForTest")
                    .email("TestTest@java.com.pl")
                    .password("TestForApplicationPassword")
                    .build();
        }
    }

    public static class AuthenticationRequestDTOFactory {

        public static AuthenticationRequestDTO.AuthenticationRequestDTOBuilder createAuthenticationRequestDTOBuilder() {
            {
                return AuthenticationRequestDTO.builder();
            }
        }
    }

    public static class ConfirmationTokenFactory {

        public static ConfirmationTokenEntity.ConfirmationTokenEntityBuilder createConfirmationTokenBuilder() {
            return ConfirmationTokenEntity.builder();
        }
    }

    public static class TopicEntityFactory {

        public static TopicEntity.TopicEntityBuilder createTopicEntityBuilder() {
            return TopicEntity.builder();
        }
    }
}
