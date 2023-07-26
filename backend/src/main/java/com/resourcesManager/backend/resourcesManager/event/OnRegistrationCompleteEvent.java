package com.resourcesManager.backend.resourcesManager.event;

import com.resourcesManager.backend.resourcesManager.model.User;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;
@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private User user;
    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.locale = locale;
    }
}
