package pl.courses.online_courses_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.courses.online_courses_backend.entity.TopicEntity;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
}
