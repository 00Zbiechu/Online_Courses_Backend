package pl.courses.online_courses_backend;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainer extends PostgreSQLContainer<TestContainer> {

    private static final String IMAGE_VERSION = "postgres:15.1-alpine3.17";
    private static TestContainer container;

    private TestContainer() {
        super(IMAGE_VERSION);
    }

    public static TestContainer getInstance() {
        if (container == null) {
            container = new TestContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
    }
}

