package pl.courses.online_courses_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OnlineCoursesBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineCoursesBackendApplication.class, args);
    }
}
