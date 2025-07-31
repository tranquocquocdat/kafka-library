// kafka-annotation/src/main/java/com/example/kafka/annotation/TopicHandler.java
package org.dat.tran.kafka.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TopicHandler {
    String topic();
}
