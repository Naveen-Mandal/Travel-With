package com.naveenmandal.TravelWith.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// ... keep your existing imports

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    
    // ... your existing dependencies (JwtUtil, MyUserDetailsService)

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtUtil.extractUsername(token);
                logger.info("Intercepted request: Validating token for user '{}'", username);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Your validation logic...
                if (jwtUtil.validateToken(token, userDetails)) {
                    logger.debug("Token validation successful for user '{}'", username);
                    // UsernamePasswordAuthenticationToken setup...
                } else {
                    logger.warn("Token validation failed for incoming user token context.");
                }
            }
        } catch (Exception ex) {
            logger.error("Security Filter Exception triggered during processing: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}