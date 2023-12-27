package com.NewFeed.backend.security;


import com.NewFeed.backend.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;
    private Message<?> exception;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var cmd = accessor.getCommand();
        if (StompCommand.CONNECT == cmd || StompCommand.SEND == cmd) {
            String jwt = extractJwtToken(accessor);
            if (jwt!=null && jwtService.validateJwtToken(jwt)){
                String username = jwtService.getUserNameFromJwtToken(jwt);
                UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(username);
                accessor.setUser(userDto);
            }
            else{
                return exception;
            }


        }
        return message;
    }

    private String extractJwtToken(StompHeaderAccessor accessor) {
        String tokenHeader = accessor.getFirstNativeHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }
        return null;
    }

}





