package com.NewFeed.backend.security;


import com.NewFeed.backend.dto.UserDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private MessageChannel clientOutboundChannel;

    @Autowired
    Logger logger;
    private Message<?> exception;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull  MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        final var cmd = accessor==null?null:accessor.getCommand();
        if (StompCommand.CONNECT == cmd || StompCommand.SEND == cmd) {
            final String sessionId = accessor.getSessionId();
            String jwt = extractJwtToken(accessor);
            try {
                if (jwt!=null){
                    jwtService.validateJwtToken(jwt);
                    String username = jwtService.getUserNameFromJwtToken(jwt);
                    UserDto userDto = (UserDto) userDetailsService.loadUserByUsername(username);
                    accessor.setUser(userDto);
                }
            }
            catch (Exception e) {
                handleAuthenticationException(e,sessionId);
            }
        }
        return message;
    }

    private void handleAuthenticationException(Exception exception,String sessionId) {
        String errorMessage = exception.getClass().getSimpleName()+" :: "+exception.getMessage();
        logger.info(errorMessage);
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setMessage(errorMessage);
        headerAccessor.setSessionId(sessionId);
        this.clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
    }
    private String extractJwtToken(StompHeaderAccessor accessor) {
        String tokenHeader = accessor.getFirstNativeHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }
        return null;
    }

}





