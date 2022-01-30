package com.curtisnewbie.module.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT based authentication processing filter
 * <p>
 * Will only process requests that have "Authorization" header
 * </p>
 *
 * @author yongjie.zhuang
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String TOKEN_HEADER = "Authorization";

    protected JwtAuthenticationFilter() {
        super(new RequestHeaderRequestMatcher(TOKEN_HEADER));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // extract JWT token
        final String token = getToken(request);

        // build Authentication object
        final JwtAuthenticationToken jwtAuthToken = new JwtAuthenticationToken(token);

        // authenticate
        return this.getAuthenticationManager().authenticate(jwtAuthToken);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    /** Get JWT Token from request */
    private String getToken(HttpServletRequest req) {
        return req.getHeader(TOKEN_HEADER);
    }
}
