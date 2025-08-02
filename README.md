# Simple Kafka Library - Complete Architecture

## 🎯 **Tổng quan Architecture**

Đây là architecture đơn giản nhất để bạn hiểu rõ cách hoạt động:

```
📦 kafka-library/
├── 📁 kafka-model/          # Data models (UserEvent, OrderEvent, Topic, EventType)
├── 📁 kafka-core/           # Core interface (EventStrategy)
├── 📁 kafka-registry/       # Registry + Annotations + CompositeKey
├── 📁 kafka-handler/        # Business logic strategies
├── 📁 kafka-client/         # Kafka producers & consumers
└── 📁 kafka-app/           # Main application + monitoring
```
kafka-library/
├── pom.xml (root)
├── kafka-model/
│   ├── pom.xml
│   └── src/main/java/org/dat/tran/kafka/model/
│       ├── EventType.java
│       ├── Topic.java  
│       ├── UserEvent.java
│       └── OrderEvent.java
├── kafka-core/
│   ├── pom.xml
│   └── src/main/java/org/dat/tran/kafka/core/
│       └── EventStrategy.java
├── kafka-registry/
│   ├── pom.xml
│   └── src/main/java/org/dat/tran/kafka/
│       ├── annotation/HandelEventStrategy.java
│       └── registry/
│           ├── TopicEventKey.java
│           └── EventStrategyRegistry.java
├── kafka-handler/
│   ├── pom.xml
│   └── src/main/java/org/dat/tran/kafka/handler/
│       ├── UserCreatedStrategy.java
│       ├── UserDeletedStrategy.java
│       ├── OrderCreatedStrategy.java
│       ├── OrderCreatedAnalyticsStrategy.java
│       └── OrderCancelledStrategy.java
├── kafka-client/
│   ├── pom.xml
│   └── src/main/java/org/dat/tran/kafka/client/
│       ├── producer/
│       │   ├── AbstractKafkaProducer.java
│       │   ├── UserEventProducer.java
│       │   └── OrderEventProducer.java
│       ├── consumer/KafkaEventConsumer.java
│       └── config/KafkaClientConfig.java
└── kafka-app/
├── pom.xml
├── src/main/java/org/dat/tran/kafka/app/
│   ├── KafkaApplication.java
│   ├── TestDataRunner.java
│   └── MonitoringController.java
└── src/main/resources/application.yml

## 🔄 **Workflow đơn giản**

### 1. **Tạo Strategy (Business Logic)**
```java
@Component
@HandelEventStrategy(topic = Topic.USER_EVENT, eventType = EventType.USER_CREATED)
public class UserCreatedStrategy implements EventStrategy<UserEvent> {
    @Override
    public void process(UserEvent event) {
        System.out.println("Processing: " + event);
        // Your business logic here
    }
}
```

### 2. **Registry tự động scan và register**
- Tìm tất cả `@HandelEventStrategy` annotations
- Tạo `TopicEventKey(Topic.USER_EVENT, EventType.USER_CREATED)`
- Map key → strategy trong `Map<TopicEventKey, List<EventStrategy<?>>>`

### 3. **Consumer nhận message và route**
```java
@KafkaListener(topics = "user-topic")
public void handleUserEvent(String message) {
    // Parse message → get EventType
    // Call registry.processEvent(Topic.USER_EVENT, eventType, event)
    // Registry tìm strategies và execute
}
```

## 🏗️ **Các thành phần chính**

### **TopicEventKey (Composite Key)**
```java
// Thay vì nested maps phức tạp:
Map<Topic, Map<EventType, List<Strategy>>> // ❌ Phức tạp

// Dùng composite key đơn giản:
Map<TopicEventKey, List<Strategy>> // ✅ Đơn giản
```

### **EventStrategyRegistry (Core)**
- **Single responsibility**: Quản lý mapping giữa key và strategies
- **O(1) lookup**: `strategies.get(key)` - cực nhanh
- **Auto-discovery**: Tự động tìm và register strategies

### **@HandelEventStrategy (Annotation)**
```java
@HandelEventStrategy(
    topic = Topic.USER_EVENT,      // Enum type-safe
    eventType = EventType.USER_CREATED,  // Enum type-safe
    priority = 10                  // Optional priority
)
```

## 🚀 **Cách chạy hệ thống**

### 1. **Setup Kafka**
```bash
# Start Kafka server
bin/kafka-server-start.sh config/server.properties

# Create topics
bin/kafka-topics.sh --create --topic user-topic --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic order-topic --bootstrap-server localhost:9092
```

### 2. **Run Application**
```bash
# From kafka-app module
mvn spring-boot:run
```

### 3. **Expected Console Output**
```
🚀 Starting Simple Kafka Application...
🚀 Initializing EventStrategyRegistry...
📡 Found 5 strategy beans

✅ Registered: userCreatedStrategy → USER_EVENT.USER_CREATED (priority: 10)
✅ Registered: userDeletedStrategy → USER_EVENT.USER_DELETED (priority: 10)
✅ Registered: orderCreatedStrategy → ORDER_EVENT.ORDER_CREATED (priority: 10)
✅ Registered: orderCreatedAnalyticsStrategy → ORDER_EVENT.ORDER_CREATED (priority: 5)
✅ Registered: orderCancelledStrategy → ORDER_EVENT.ORDER_CANCELLED (priority: 10)

==================================================
📊 REGISTRY STATISTICS
==================================================
✅ Total strategies: 5
🗺️ Unique combinations: 4
🎧 Active topics: 2

📋 Registered Combinations:
  USER_EVENT.USER_CREATED → 1 strategies
  USER_EVENT.USER_DELETED → 1 strategies
  ORDER_EVENT.ORDER_CREATED → 2 strategies
  ORDER_EVENT.ORDER_CANCELLED → 1 strategies
==================================================

🧪 Starting End-to-End Test...
📊 Registry Information:
Active topics: [user-topic, order-topic]
Strategy combinations:
  ✅ USER_EVENT.USER_CREATED
  ✅ USER_EVENT.USER_DELETED
  ✅ ORDER_EVENT.ORDER_CREATED
  ✅ ORDER_EVENT.ORDER_CANCELLED

👤 Testing User Events...
📤 Producing to topic user-topic: UserEvent{userId='u123', username='alice', eventType=USER_CREATED}
📨 Received USER event from user-topic
🔄 Processing USER_EVENT.USER_CREATED with 1 strategies
🎉 UserCreatedStrategy processing: UserEvent{userId='u123', username='alice', eventType=USER_CREATED}
📧 Sending welcome email to: alice
👤 Creating user profile for: u123
📊 Logging user creation: u123
✅ User creation completed for: alice

🛒 Testing Order Events...
📤 Producing to topic order-topic: OrderEvent{orderId='o789', amount=99.99, eventType=ORDER_CREATED}
📨 Received ORDER event from order-topic
🔄 Processing ORDER_EVENT.ORDER_CREATED with 2 strategies
🛒 OrderCreatedStrategy processing: OrderEvent{orderId='o789', amount=99.99, eventType=ORDER_CREATED}
✔️ Validating order: o789
📦 Reserving inventory for order: o789
💳 Processing payment: $99.99
📧 Sending confirmation for order: o789
✅ Order created successfully: o789
📊 OrderCreatedAnalyticsStrategy processing: OrderEvent{orderId='o789', amount=99.99, eventType=ORDER_CREATED}
📊 Tracking metrics for order: o789
📈 Updating dashboards with order: o789
🎯 Triggering recommendations for order: o789
📈 Analytics completed for order: o789

✅ End-to-End test completed!
```

## 📊 **Monitoring Endpoints**

### REST APIs có sẵn:
```bash
# System status
GET http://localhost:8080/admin/kafka/status

# Strategy coverage
GET http://localhost:8080/admin/kafka/coverage  

# Health check
GET http://localhost:8080/admin/kafka/health
```

## 💡 **Cách thêm Strategy mới**

### Bước 1: Tạo Strategy class
```java
@Component
@HandelEventStrategy(topic = Topic.USER_EVENT, eventType = EventType.USER_CREATED, priority = 5)
public class UserCreatedEmailStrategy implements EventStrategy<UserEvent> {
    @Override
    public void process(UserEvent event) {
        // Send email logic
    }
}
```

### Bước 2: Restart application
- Registry sẽ tự động tìm và register strategy mới
- Không cần config gì thêm!

## 🎯 **Key Benefits**

### 1. **Cực kỳ đơn giản**
- Chỉ cần tạo Strategy + Annotation
- Registry tự động handle tất cả

### 2. **Type-safe**
- Topic và EventType là enums
- Compile-time validation

### 3. **Performance cao**
- O(1) strategy lookup
- Single hash computation

### 4. **Dễ debug**
- Flat structure, clear logs
- REST endpoints để monitor

### 5. **Scalable**
- Multiple strategies per event
- Priority-based execution
- Easy to add new events/topics

## 🏆 **Kết luận Architecture**

**Đây là architecture đơn giản nhất có thể:**
- ✅ **6 modules** với responsibility rõ ràng
- ✅ **CompositeKey** thay vì nested maps phức tạp
- ✅ **Auto-discovery** strategies qua annotations
- ✅ **Single registry** quản lý tất cả
- ✅ **Minimal configuration** - chỉ cần annotation

**Workflow cực đơn giản:**
1. **Tạo Strategy** với `@HandelEventStrategy`
2. **Registry tự động scan** và register
3. **Consumer nhận message** và delegate đến registry
4. **Registry tìm strategies** và execute theo priority

**Không cần configuration files, không cần manual wiring, tất cả đều automatic!** 🎉