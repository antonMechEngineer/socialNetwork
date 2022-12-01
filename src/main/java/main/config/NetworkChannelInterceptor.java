package main.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import main.security.jwt.JWTUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NetworkChannelInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && accessor.getCommand() == StompCommand.CONNECT) {
            final String token = accessor.getFirstNativeHeader("token");
            accessor.setUser(jwtUtil.getAuth(token));
        }
        return message;
    }
}
