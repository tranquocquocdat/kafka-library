package org.dat.tran.kafka.app;


import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.OrderEvent;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.model.UserEvent;
import org.dat.tran.kafka.registry.EventStrategyRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple REST endpoints for monitoring the system
 */
@RestController
@RequestMapping("/admin/kafka")
public class MonitoringController {

    @Autowired
    private EventStrategyRegistry registry;

    /**
     * Get system status
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("activeTopics", registry.getActiveTopics().size());
        status.put("topicNames", registry.getActiveTopics());
        status.put("status", "UP");

        return status;
    }

    /**
     * Get strategy coverage information
     */
    @GetMapping("/coverage")
    public Map<String, Object> getCoverage() {
        Map<String, Object> coverage = new HashMap<>();

        // Check each topic-event combination
        Map<String, Map<String, Boolean>> topicCoverage = new HashMap<>();

        for (Topic topic : Topic.values()) {
            Map<String, Boolean> eventCoverage = new HashMap<>();

            for (EventType eventType : topic.getEventTypes()) {
                boolean hasStrategies = registry.hasStrategies(topic, eventType);
                eventCoverage.put(eventType.name(), hasStrategies);
            }

            topicCoverage.put(topic.name(), eventCoverage);
        }

        coverage.put("coverage", topicCoverage);
        return coverage;
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public Map<String, String> getHealth() {
        Map<String, String> health = new HashMap<>();

        int activeTopics = registry.getActiveTopics().size();

        if (activeTopics > 0) {
            health.put("status", "UP");
            health.put("message", "System is healthy with " + activeTopics + " active topics");
        } else {
            health.put("status", "DOWN");
            health.put("message", "No active topics found");
        }

        return health;
    }
}