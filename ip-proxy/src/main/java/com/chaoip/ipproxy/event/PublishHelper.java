package com.chaoip.ipproxy.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 统一事件发布类，方便排查
 */
@Component
public final class PublishHelper {
    private static ApplicationEventPublisher eventPublisher;

    public PublishHelper(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public static void publish(Object obj) {
        if (eventPublisher == null) {
            throw new RuntimeException("尚未初始化，不能发事件");
        }
        eventPublisher.publishEvent(obj);
    }
}
