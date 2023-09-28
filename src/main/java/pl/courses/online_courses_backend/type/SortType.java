package pl.courses.online_courses_backend.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {

    @JsonProperty("title")
    TITLE("title"),

    @JsonProperty("startDate")
    START_DATE("startDate"),

    @JsonProperty("endDate")
    END_DATE("endDate"),

    @JsonProperty("topic")
    TOPIC("topic");

    private final String fieldName;
}
