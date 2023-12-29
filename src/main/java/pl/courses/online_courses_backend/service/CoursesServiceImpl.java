package pl.courses.online_courses_backend.service;

import com.google.common.collect.Sets;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.authentication.CurrentUser;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.entity.CourseUsersEntity;
import pl.courses.online_courses_backend.entity.FileEntity;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.entity.key.CourseUsersPK;
import pl.courses.online_courses_backend.event.CourseConfirmationDTO;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.CourseMapper;
import pl.courses.online_courses_backend.mapper.TopicMapper;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.model.FileDataDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.ParticipantDTO;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.ParticipantsDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;
import pl.courses.online_courses_backend.photo.PhotoCompressor;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;
import pl.courses.online_courses_backend.repository.TopicRepository;
import pl.courses.online_courses_backend.repository.UserRepository;
import pl.courses.online_courses_backend.type.OrderType;
import pl.courses.online_courses_backend.validator.CourseAccessValidator;
import pl.courses.online_courses_backend.validator.FileValidator;
import pl.courses.online_courses_backend.validator.TopicValidator;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoursesServiceImpl extends AbstractService<CourseEntity, CourseDTO> implements CourseService {

    private final CurrentUser currentUser;

    private final CourseRepository courseRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final CourseMapper courseMapper;

    private final TopicMapper topicMapper;

    private final TopicValidator topicValidator;

    private final FileValidator fileValidator;

    private final CourseAccessValidator courseAccessValidator;

    private final PhotoCompressor photoCompressor;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Value("${online-courses.confirmation.token.course.participant.expires.time.day}")
    private Long expirationCourseParticipation;

    @Value("${online-courses.participation.token.link}")
    private String confirmationLink;

    @Override
    protected JpaRepository<CourseEntity, Long> getRepository() {
        return courseRepository;
    }

    @Override
    protected BaseMapper<CourseEntity, CourseDTO> getMapper() {
        return courseMapper;
    }

    @Override
    public Page<CourseWithAuthorDTO> findCoursesPage(PaginationForCourseListDTO paginationForCourseListDTO) {
        PageRequest pageRequest = buildPageRequestForCoursePage(paginationForCourseListDTO);
        Page<CourseEntity> courses = courseRepository.findActiveCourses(pageRequest);
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
        if (editCourseDTO.getPassword() != null && !editCourseDTO.getPassword().isEmpty()) {
            courseEntity.setPassword(passwordEncoder.encode(courseEntity.getPassword()));
        }
        var result = courseRepository.save(courseEntity);
        return courseMapper.toDTO(result);
    }

    @Override
    public CourseWithAuthorDTO getCourse(Long courseId, String password) {
        var courseEntity = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        courseAccessValidator.validateCourseAccess(courseEntity, currentUser, passwordEncoder, password);

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
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
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

    private PageRequest buildPageRequestForCoursePage(PaginationForCourseListDTO paginationForCourseListDTO) {
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
    public TopicsDTO addTopic(Long courseId, MultipartFile[] files, AddTopicDTO addTopicDTO) {

        var courseEntity = findCourseOfUser(courseId);

        topicValidator.validateIsTopicUnique(addTopicDTO, courseEntity);

        var topicEntity = topicMapper.toEntity(addTopicDTO);
        topicEntity.setFiles(buildAttachmentsList(topicEntity, files));
        topicEntity.setCourseEntity(courseEntity);

        if (courseEntity.getTopics() == null) {
            courseEntity.setTopics(Sets.newHashSet(topicEntity));
        } else {
            courseEntity.getTopics().add(topicEntity);
        }

        var result = courseRepository.save(courseEntity);

        var topicList = result.getTopics().stream().map(topicMapper::toDTO)
                .sorted(Comparator.comparing(TopicDTO::getId))
                .toList();

        return TopicsDTO.builder().topics(topicList).build();
    }

    private Set<FileEntity> buildAttachmentsList(TopicEntity topicEntity, MultipartFile[] files) {

        Set<FileEntity> attachmentList = Sets.newHashSet();

        if (files == null || !(files.length > 0)) {
            return attachmentList;
        }

        for (MultipartFile file : files) {
            Try.run(() -> {
                fileValidator.validateFile(file);
                var fileEntity = FileEntity.builder()
                        .name(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                        .type(file.getContentType())
                        .data(file.getBytes())
                        .build();

                fileEntity.setTopicEntity(topicEntity);
                attachmentList.add(fileEntity);

            }).onFailure(error -> {
                throw new CustomErrorException("file", ErrorCodes.WRONG_FORMAT, HttpStatus.BAD_REQUEST);
            });
        }
        return attachmentList;
    }

    @Override
    public TopicsDTO getTopics(Long courseId, String password) {
        var courseEntity = courseRepository.findById(courseId).orElseThrow(
                () -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );
        courseAccessValidator.validateCourseAccess(courseEntity, currentUser, passwordEncoder, password);
        var topics = courseEntity.getTopics().stream().map(topicMapper::toDTO).toList();
        return TopicsDTO.builder().topics(topics).build();
    }

    @Override
    public FileDataDTO getAttachment(Long courseId, Long topicId, Long fileId, String password) {
        var courseEntity = courseRepository.findById(courseId).orElseThrow(
                () -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        courseAccessValidator.validateCourseAccess(courseEntity, currentUser, passwordEncoder, password);

        var topicEntity = courseEntity.getTopics().stream().filter(topic -> topic.getId().equals(topicId)).findFirst().orElseThrow(
                () -> new CustomErrorException("topic", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        var fileEntity = topicEntity.getFiles().stream().filter(file -> file.getId().equals(fileId)).findFirst().orElseThrow(
                () -> new CustomErrorException("file", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        return FileDataDTO.builder().name(fileEntity.getName()).data(fileEntity.getData()).type(fileEntity.getType()).build();
    }

    @Override
    public TopicsDTO deleteTopic(Long courseId, Long topicId) {
        var courseEntity = findCourseOfUser(courseId);
        var topicEntity = courseEntity.getTopics().stream().filter(topic -> topic.getId().equals(topicId)).findFirst().orElseThrow(
                () -> new CustomErrorException("topic", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        courseEntity.getTopics().remove(topicEntity);
        topicRepository.deleteById(topicEntity.getId());

        return TopicsDTO.builder().topics(courseEntity.getTopics().stream().map(topicMapper::toDTO)
                .sorted(Comparator.comparing(TopicDTO::getId))
                .toList()).build();
    }

    @Override
    public ParticipantsDTO getCourseParticipants(Long courseId) {
        var courseEntity = findCourseOfUser(courseId);
        return ParticipantsDTO.builder().participants(buildCourseParticipantsList(courseEntity)).build();
    }

    private List<ParticipantDTO> buildCourseParticipantsList(CourseEntity courseEntity) {
        List<CourseUsersEntity> courseUsersEntities = courseEntity.getCourseUser().stream()
                .filter(courseUsersEntity -> !courseUsersEntity.isOwner())
                .filter(CourseUsersEntity::isParticipant)
                .filter(courseUsersEntity -> courseUsersEntity.getParticipantConfirmedAt() != null)
                .toList();

        return courseUsersEntities.stream().map(user -> ParticipantDTO.builder()
                .userId(user.getCourseUsersPK().getUserEntity().getId())
                .username(user.getCourseUsersPK().getUserEntity().getUsername())
                .photo(user.getCourseUsersPK().getUserEntity().getPhoto())
                .build()
        ).toList();
    }

    @Override
    public void addCourseParticipant(Long courseId, String username) {
        var courseEntity = findCourseOfUser(courseId);
        var userEntity = userRepository.findActiveAccountByUsername(username)
                .orElseThrow(() -> new CustomErrorException("user", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        validateDuplicateUserParticipation(username, courseEntity);

        var activationToken = UUID.randomUUID().toString();

        var courseUsersEntity = CourseUsersEntity.builder()
                .courseUsersPK(CourseUsersPK.builder()
                        .courseEntity(courseEntity)
                        .userEntity(userEntity)
                        .build()
                )
                .owner(Boolean.FALSE)
                .participant(Boolean.FALSE)
                .token(activationToken)
                .tokenExpiresAt(LocalDateTime.now().plusDays(expirationCourseParticipation))
                .build();

        courseEntity.getCourseUser().add(courseUsersEntity);
        courseRepository.save(courseEntity);

        emailService.participantConfirmation(CourseConfirmationDTO.newBuilder()
                .setMail(userEntity.getEmail())
                .setTitle(courseEntity.getTitle())
                .setConfirmationLink(confirmationLink + activationToken)
                .build());
    }

    private void validateDuplicateUserParticipation(String username, CourseEntity courseEntity) {
        var duplicateUser = courseEntity.getCourseUser().stream()
                .filter(courseUsersEntity -> courseUsersEntity.getCourseUsersPK().getUserEntity().getUsername().equals(username))
                .findFirst();

        if (duplicateUser.isPresent()) {
            throw new CustomErrorException("courseUserEntity", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }
    }
}
