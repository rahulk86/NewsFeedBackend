package com.NewFeed.backend.security;

import com.NewFeed.backend.configuration.AppProperties;
import com.NewFeed.backend.dto.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    @Autowired
    Logger logger;

    @Autowired
    private AppProperties appProperties;
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public ResponseCookie generateJwtCookie(UserDto userDto) {
        String jwt = generateJwtToken(userDto);
        return generateJwtCookie(appProperties.getAuth().getJwtCookieName(),jwt,"/api");
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateJwtCookie(appProperties.getAuth().getJwtRefreshCookieName(), refreshToken, "/api/users/refresh");
    }
    public ResponseCookie generateJwtCookie(String name,String jwt, String path) {
        Duration maxAge = Duration.ofDays(1);
        return ResponseCookie
                .from(name, jwt)
                .path(path)
                .maxAge(maxAge)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();
    }
    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, appProperties.getAuth().getJwtCookieName());
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, appProperties.getAuth().getJwtRefreshCookieName());
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(appProperties.getAuth().getJwtCookieName(), null).path("/api").build();
        return cookie;
    }
    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(appProperties.getAuth().getJwtRefreshCookieName(), null).path("/api/auth/refreshtoken").build();
        return cookie;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(key()).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public String generateJwtToken(UserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().
                setClaims(claims).
                setSubject((userDto.getUsername())).
                setIssuedAt(new Date()).
                setExpiration(new Date((new Date()).getTime() + appProperties.getAuth().getJwtExpirationMs()
                )).
                signWith(key(), SignatureAlgorithm.HS256).
                compact();
    }
    public boolean validateJwtToken(String authToken) {
        try {
           // Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return !isTokenExpired(authToken);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getAuth().getJwtSecret()));
    }


}
