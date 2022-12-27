package soialNetworkApp.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic notificationsTopic(){
        return TopicBuilder.name("notifications").build();
    }

//    @Bean
//    public NewTopic messagesTopic(){
//        return TopicBuilder.name("messages").build();
//    }


}
