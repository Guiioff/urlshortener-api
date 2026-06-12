package com.devgui.urlshortener.infrastructure.ratelimiter;

import com.devgui.urlshortener.api.v1.exception.ErrorResponse;
import tools.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;
    private final ObjectMapper objectMapper;

    public RateLimitFilter(RateLimitingService rateLimitingService, ObjectMapper objectMapper) {
        this.rateLimitingService = rateLimitingService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = this.getClientIp(request);
        Bucket tokenBucket = rateLimitingService.resolveBucket(clientIp);

        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()){
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            var waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setContentType("application/json");

            ErrorResponse error = ErrorResponse.of(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "API request quota exceeded.",
                    request.getRequestURI()
            );

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), error);
        }
    }

    private String getClientIp(HttpServletRequest request){
        String xfHeader = request.getHeader("X-Fowarded-For");
        if (xfHeader == null || xfHeader.isEmpty()){
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0].trim();
    }
}
