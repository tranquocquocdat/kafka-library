// kafka-registry/src/main/java/com/example/kafka/registry/KafkaHandlerRegistry.java
package org.dat.tran.kafka.handler;


import org.dat.tran.kafka.annotation.TopicHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Scans the Spring context for beans annotated with @TopicHandler
 * and maps topics to handler instances.
 */
@Component
public class KafkaHandlerRegistry implements ApplicationContextAware {

    private final Map<String, List<KafkaMessageHandler>> handlersByTopic = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // Find all beans annotated with @TopicHandler
        Map<String, Object> beans = ctx.getBeansWithAnnotation(TopicHandler.class);
        for (Object bean : beans.values()) {
            TopicHandler annotation = bean.getClass().getAnnotation(TopicHandler.class);
            if (annotation != null && bean instanceof KafkaMessageHandler) {
                String topic = annotation.topic();
                handlersByTopic
                    .computeIfAbsent(topic, k -> new ArrayList<>())
                    .add((KafkaMessageHandler) bean);
            }
        }
    }

    /**
     * Get handlers registered for a given topic.
     */
    public List<KafkaMessageHandler> getHandlers(String topic) {
        return handlersByTopic.getOrDefault(topic, Collections.emptyList());
    }

    /**
     * Returns all registered topics.
     */
    public Set<String> getTopics() {
        return handlersByTopic.keySet();
    }

}
