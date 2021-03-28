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
        publish(EventType.Other, obj);
    }

    public static void publish(EventType type, Object obj) {
        if (eventPublisher == null) {
            throw new RuntimeException("尚未初始化，不能发事件");
        }
        eventPublisher.publishEvent(obj);
    }

    public enum EventType {
        NewUser("新用户注册"),
        ChangePwd("改密码"),
        ChangeSK("改SecurityKey"),
        CreateRealNameOrder("发起实名认证"),
        SuccessRealName("实名认证成功"),
        DisableUser("禁用用户"),
        EnableUser("启用用户"),
        ChangeRole("改用户角色"),
        ResetPwd("重置用户密码"),
        SetRealName("直接设置用户实名"),
        AddMoney("用户充值"),
        Other("其它");

        private String title;

        EventType(String title) {
            this.title = title;
        }
    }
}
