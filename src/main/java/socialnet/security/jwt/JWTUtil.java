package socialnet.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import socialnet.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTUtil {

    @Value("${auth.secret}")
    private String secret;
    @Value("${auth.time-to-live}")
    private Long timeToLive;

    private final UserDetailsServiceImpl userDetailsService;

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + timeToLive))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String extractUserName(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean isValidToken(String token) {
            return getTokenBody(token).getExpiration().after(new Date()) &&
                    !getTokenBody(token).isEmpty();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Authentication getAuth(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(extractUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
