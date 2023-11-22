package pl.courses.online_courses_backend.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${online-courses.kafka.topics.email.name}")
    private String topicName;

    @Value("${online-courses.kafka.topics.email.partitions}")
    private int partition;

    @Value("${online-courses.kafka.topics.email.replicationFactory}")
    private short replicationFactory;


    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder.name(topicName).partitions(partition).replicas(replicationFactory).build();
    }
}
