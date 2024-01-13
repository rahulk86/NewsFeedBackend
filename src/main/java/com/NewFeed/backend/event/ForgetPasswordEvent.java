package com.NewFeed.backend.event;

import com.NewFeed.backend.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ForgetPasswordEvent extends ApplicationEvent {
    private UserDto user;
    private String applicationUrl;
    private String verificationToken;
    public ForgetPasswordEvent(UserDto user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
    public String getApplicationUrl(String verificationToken){
       this.verificationToken  = verificationToken;
       return applicationUrl+"/resetPassword?token="+this.verificationToken;
    }
}
