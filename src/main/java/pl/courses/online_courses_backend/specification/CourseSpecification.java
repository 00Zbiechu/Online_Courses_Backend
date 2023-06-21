package pl.courses.online_courses_backend.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.entity.UsersEntity;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseSpecification implements Specification<CoursesEntity> {

    private final List<SearchCriteria> searchCriteria;

    public void add(SearchCriteria criteria) {

        if (criteria.getValue() != null && !criteria.getValue().toString().isEmpty()) {
            searchCriteria.add(criteria);
        }

    }

    public void clear() {
        searchCriteria.clear();
    }

    @Override
    public Predicate toPredicate(Root<CoursesEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : searchCriteria) {
            if (criteria.getKey().contains(".")) {
                String[] joinKeys = criteria.getKey().split("\\.");
                Join<CoursesEntity, UsersEntity> join = root.join(joinKeys[0]);
                predicates.add(builder.like(builder.lower(join.get(joinKeys[1])), "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperations.MATCH)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperations.MATCH_END)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperations.MATCH_START)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperations.EQUAL)) {
                predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
