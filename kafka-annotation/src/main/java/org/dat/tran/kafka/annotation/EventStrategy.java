// kafka-annotation/src/main/java/com/example/kafka/annotation/EventStrategy.java
package org.dat.tran.kafka.annotation;


import org.dat.tran.kafka.model.EventType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventStrategy {
    EventType value();
}
