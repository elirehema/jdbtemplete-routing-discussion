package org.ospic.platform.util;

import org.ospic.platform.constant.JWTConstants;
import org.ospic.platform.tenant.app.security.services.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Md. Amran Hossain
 */
@Component
public class JwtTokenUtil implements Serializable {

    private int refreshExpirationDateInMs;

    @Value("${mod.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mod.app.jwtExpirationMs}")
    private int jwtExpirationInMs;

    @Value("${mod.app.jwtRefreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    private static final long serialVersionUID = -2550185165626007488L;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWTConstants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public String generateJwtToken(Authentication authentication, String tenantId) {

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return generateToken(userDetails, tenantId);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails, String tenantId) {
        //Map<String, Object> claims = new HashMap<>();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername()).setAudience(tenantId);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }

        return generateToken(claims, userDetails.getUsername(), tenantId);
    }

    public String generateToken(Map<String, Object> claims,String userName, String tenantOrClientId) {
        return doGenerateToken(claims,userName,tenantOrClientId);
    }

    private String doGenerateToken(Map<String, Object> claims,String subject, String tenantOrClientId) {

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("system")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, JWTConstants.SIGNING_KEY)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
