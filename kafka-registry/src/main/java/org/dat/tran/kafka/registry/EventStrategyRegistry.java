package org.dat.tran.kafka.registry;

import org.dat.tran.kafka.core.EventStrategy;
import org.dat.tran.kafka.model.EventType;
import org.dat.tran.kafka.model.Topic;
import org.dat.tran.kafka.registry.annotation.HandelEventStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class EventStrategyRegistry implements ApplicationContextAware {

    // 🎯 SINGLE MAP with composite key - This is the magic!
    private final Map<TopicEventKey, List<EventStrategy<?>>> strategies = new ConcurrentHashMap<>();

    // Simple statistics
    private int totalRegistrations = 0;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        System.out.println("🚀 Initializing EventStrategyRegistry...");

        // Find all beans with our annotation
        Map<String, Object> strategyBeans = ctx.getBeansWithAnnotation(HandelEventStrategy.class);

        System.out.println("📡 Found " + strategyBeans.size() + " strategy beans");

        // Process each strategy bean
        strategyBeans.forEach(this::registerStrategy);

        // Sort all strategies by priority
        strategies.values().forEach(this::sortByPriority);

        logStatistics();
    }

    private void registerStrategy(String beanName, Object bean) {
        try {
            // Validate bean is actually a strategy
            if (!(bean instanceof EventStrategy)) {
                System.out.println("⏭️ Skipped: " + beanName + " - Not an EventStrategy");
                return;
            }

            // Get annotation
            HandelEventStrategy annotation = bean.getClass().getAnnotation(HandelEventStrategy.class);
            if (annotation == null) {
                System.out.println("⏭️ Skipped: " + beanName + " - No annotation");
                return;
            }

            // Validate topic-eventType combination
            if (!annotation.topic().supports(annotation.eventType())) {
                System.err.println("❌ Failed: " + beanName + " - Invalid topic-eventType combination");
                return;
            }

            // 🎯 Register with composite key - SO SIMPLE!
            TopicEventKey key = TopicEventKey.from(annotation);
            strategies.computeIfAbsent(key, k -> new ArrayList<>())
                     .add((EventStrategy<?>) bean);

            totalRegistrations++;

            System.out.println("✅ Registered: " + beanName + " → " + key +
                             " (priority: " + annotation.priority() + ")");

        } catch (Exception e) {
            System.err.println("❌ Error registering " + beanName + ": " + e.getMessage());
        }
    }

    private void sortByPriority(List<EventStrategy<?>> strategyList) {
        strategyList.sort((s1, s2) -> {
            int p1 = s1.getClass().getAnnotation(HandelEventStrategy.class).priority();
            int p2 = s2.getClass().getAnnotation(HandelEventStrategy.class).priority();
            return Integer.compare(p2, p1); // Higher priority first
        });
    }

    // ===== PUBLIC API - Clean and Simple! =====

    /**
     * 🎯 Get strategies for Topic + EventType - O(1) lookup!
     */
    public List<EventStrategy<?>> getStrategies(Topic topic, EventType eventType) {
        TopicEventKey key = TopicEventKey.of(topic, eventType);
        return strategies.getOrDefault(key, Collections.emptyList());
    }

    /**
     * 🎯 Process message - Single method call!
     */
    public void processEvent(Topic topic, EventType eventType, Object event) {
        TopicEventKey key = TopicEventKey.of(topic, eventType);
        List<EventStrategy<?>> strategyList = strategies.get(key);

        if (strategyList == null || strategyList.isEmpty()) {
            System.out.println("⚠️ No strategies found for " + key);
            return;
        }

        System.out.println("🔄 Processing " + key + " with " + strategyList.size() + " strategies");

        // Execute all strategies in priority order
        for (EventStrategy<?> strategy : strategyList) {
            try {
                @SuppressWarnings("unchecked")
                EventStrategy<Object> typedStrategy = (EventStrategy<Object>) strategy;
                typedStrategy.process(event);
            } catch (Exception e) {
                System.err.println("❌ Error in strategy: " + e.getMessage());
            }
        }
    }

    /**
     * Get all active topics for Kafka consumer configuration
     */
    public Set<String> getActiveTopics() {
        return strategies.keySet().stream()
                .map(TopicEventKey::getTopicName)
                .collect(Collectors.toSet());
    }

    /**
     * Check if combination has strategies
     */
    public boolean hasStrategies(Topic topic, EventType eventType) {
        return strategies.containsKey(TopicEventKey.of(topic, eventType));
    }

    private void logStatistics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 REGISTRY STATISTICS");
        System.out.println("=".repeat(50));
        System.out.println("✅ Total strategies: " + totalRegistrations);
        System.out.println("🗺️ Unique combinations: " + strategies.size());
        System.out.println("🎧 Active topics: " + getActiveTopics().size());

        if (!strategies.isEmpty()) {
            System.out.println("\n📋 Registered Combinations:");
            strategies.forEach((key, strategyList) ->
                System.out.println("  " + key + " → " + strategyList.size() + " strategies"));
        }

        System.out.println("=".repeat(50) + "\n");
    }
}