package pl.courses.online_courses_backend.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${online-courses.kafka.topics.registration.email.name}")
    private String registrationTopic;

    @Value("${online-courses.kafka.topics.participation.email.name}")
    private String participationTopic;

    @Value("${online-courses.kafka.topics.email.partitions}")
    private int partition;

    @Value("${online-courses.kafka.topics.email.replicationFactory}")
    private short replicationFactory;

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder.name(registrationTopic).partitions(partition).replicas(replicationFactory).build();
    }

    @Bean
    public NewTopic participationTopic() {
        return TopicBuilder.name(participationTopic).partitions(partition).replicas(replicationFactory).build();
    }
}
