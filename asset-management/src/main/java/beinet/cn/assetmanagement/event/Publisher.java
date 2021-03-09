package beinet.cn.assetmanagement.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 单独定义一个事件发送类，方便问题排查，哪些地方发送了事件
 */
@Component
public class Publisher {
    private static ApplicationEventPublisher eventPublisher; // 发送事件用

    public Publisher(ApplicationEventPublisher eventPublisher) {
        Publisher.eventPublisher = eventPublisher;
    }

    public static void publishEvent(Object eventObj) {
        if (eventPublisher == null) {
            throw new RuntimeException("Spring bean尚未初始化完成，无法调用");
        }
        eventPublisher.publishEvent(eventObj);
    }
}
