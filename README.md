# Simple Kafka Library — 1‑Pager Architecture

## 🎯 Goal
Provide **annotation‑driven routing** for Kafka. Consumers only parse `(topic + eventType)` and **delegate** to `EventStrategyRegistry`, which finds the right **strategy list** and executes them by **priority**. No verbose configs.

## 🧩 Module Map (one line each)
- **kafka-model**: enums `Topic`, `EventType`; payloads `UserEvent`, `OrderEvent`.
- **kafka-core**: contract `EventStrategy<T>`.
- **kafka-registry**: `@HandelEventStrategy`, `TopicEventKey(topic, eventType)`, `EventStrategyRegistry` (scan & register).
- **kafka-handler**: where your **business strategies** live.
- **kafka-client**: producers/consumers + `KafkaClientConfig`.
- **kafka-app**: Spring Boot entry, test data, and monitoring REST.

## 🔄 End‑to‑End Flow
1) **Producer** sends event → Kafka (`user-topic` / `order-topic`).
2) **Consumer** receives message → parse `eventType` and payload.
3) Call `registry.processEvent(topic, eventType, payload)`.
4) **Registry** does O(1) lookup in `Map<TopicEventKey, List<EventStrategy>>`, sorts by `priority`, executes strategies.

```
📦 kafka-library/
├── 📁 kafka-model/          # Data models (UserEvent, OrderEvent, Topic, EventType)
├── 📁 kafka-core/           # Core interface (EventStrategy)
├── 📁 kafka-registry/       # Registry + Annotations + CompositeKey
├── 📁 kafka-handler/        # Business logic strategies
├── 📁 kafka-client/         # Kafka producers & consumers
└── 📁 kafka-app/            # Main application + monitoring
```

## 🗺️ Core Types & Registry
- **CompositeKey** (`TopicEventKey`) avoids nested maps and keeps **lookup O(1)**.
- **Auto‑discovery**: at startup, Spring scans beans annotated with `@HandelEventStrategy` and **registers** them.
- **Type‑safe**: `Topic` and `EventType` are enums.
- **Clear logs & REST**: easier troubleshooting and coverage checks.

### Strategy Annotation
```java
@HandelEventStrategy(
  topic = Topic.USER_EVENT,       // enum, type-safe
  eventType = EventType.USER_CREATED,
  priority = 10                   // optional
)
```

### Registry Contract
```java
public interface EventStrategy<T> {
  void process(T event);
}
```

## ➕ Add a New Strategy (just 1 class + 1 annotation)
```java
@Component
@HandelEventStrategy(topic = Topic.USER_EVENT, eventType = EventType.USER_CREATED, priority = 5)
public class UserCreatedEmailStrategy implements EventStrategy<UserEvent> {
  @Override public void process(UserEvent e) {
    // send welcome email, audit, etc.
  }
}
```
Restart the app → the registry **auto‑registers** it. No manual wiring.

## ▶️ Run Locally
```bash
# Start Kafka (example)
bin/kafka-server-start.sh config/server.properties

# Create topics
bin/kafka-topics.sh --create --topic user-topic --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic order-topic --bootstrap-server localhost:9092

# Run Spring Boot (from kafka-app)
mvn spring-boot:run
```

## 🩺 Monitoring Endpoints
- `GET /admin/kafka/status` – system status
- `GET /admin/kafka/coverage` – strategy coverage
- `GET /admin/kafka/health` – health check

## ✅ Why This Stays Simple (and Scales)
- **Flat structure, clear responsibilities** across 6 modules.
- **Annotation → auto‑register** keeps boilerplate near zero.
- **O(1) lookup** via `TopicEventKey`.
- **Multiple strategies per event** with **priority‑based** execution.
- Easy to extend, test, and observe.

> Optional clean‑ups: keep annotation name consistent (`@HandelEventStrategy` is used here; consider `@HandleEventStrategy` if you plan to rename), and ensure deterministic sorting by priority (e.g., descending) before execution.
