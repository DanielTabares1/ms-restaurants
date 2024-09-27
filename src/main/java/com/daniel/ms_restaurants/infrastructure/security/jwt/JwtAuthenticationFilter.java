package com.daniel.ms_restaurants.infrastructure.security.jwt;

import com.daniel.ms_restaurants.infrastructure.feignclient.CustomUserDetailsService;
import com.daniel.ms_restaurants.infrastructure.security.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final CustomUserDetailsService userDetailsService;

        private final List<String> excludedPrefixes = Arrays.asList(SecurityConstants.WHITE_LIST_URL);

        private final AntPathMatcher pathMatcher = new AntPathMatcher();

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain)
                throws ServletException, IOException {

            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = getToken(request);
            JwtTokenHolder.setToken(jwt);

            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }
            filterChain.doFilter(request, response);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            for (String prefix : excludedPrefixes) {
                if (pathMatcher.match(prefix, request.getServletPath())) {
                    return true;
                }
            }
            return false;
        }

        private String getToken(HttpServletRequest request) {
            return request.getHeader(SecurityConstants.AUTHORIZATION_HEADER).substring(7);
        }

    }
