package pl.courses.online_courses_backend.kafka.producer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import pl.courses.online_courses_backend.model.CourseConfirmationDTO;
import pl.courses.online_courses_backend.model.UsernameAndMailDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.producer.schema.registry.url}")
    String schemaRegistryUrl;

    private Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", schemaRegistryUrl);
        return props;
    }

    @Bean
    public ProducerFactory<String, UsernameAndMailDTO> registrationFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, UsernameAndMailDTO> registrationTemplate(ProducerFactory<String, UsernameAndMailDTO> registrationFactory) {
        return new KafkaTemplate<>(registrationFactory);
    }

    @Bean
    public ProducerFactory<String, CourseConfirmationDTO> courseConfirmationFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, CourseConfirmationDTO> courseConfirmationTemplate(ProducerFactory<String, CourseConfirmationDTO> courseConfirmationFactory) {
        return new KafkaTemplate<>(courseConfirmationFactory);
    }
}
