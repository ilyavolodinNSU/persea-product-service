package ru.persea.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

@Configuration
public class KafkaConfiguration {
    // @Bean
    // public NewTopics userActionsTopic() {
    //     return new NewTopics(
    //         TopicBuilder.name("user-actions")
    //         .replicas(1)
    //         .partitions(1)
    //         .build()
    //     );
    // }
}
