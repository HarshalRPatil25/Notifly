package com.notifly.backend.ratelimiter;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

  private final RedisTemplate<String, String> redisTemplate;

    private  Long MAX_ATTEMPTS = 5L;

    public boolean isKeyPresent(String username) {
        try {
            Boolean keyExists = redisTemplate.hasKey(username);
            logger.info("Checking if key exists for user: {} -> {}", username, keyExists);
            return Boolean.TRUE.equals(keyExists);
        } catch (Exception e) {
            logger.error("Error checking key presence for user: {}", username, e);
            return false;
        }
    }

    public boolean createKey(String username) {
        try {
            redisTemplate.opsForValue().set(username, String.valueOf(MAX_ATTEMPTS));
            logger.info("Created key for user: {} with attempts: {}", username, MAX_ATTEMPTS);
            return true;
        } catch (Exception e) {
            logger.error("Error creating key for user: {}", username, e);
            return false;
        }
    }

    public boolean decreaseAttempt(String username) {
    try {
        String currentVal = redisTemplate.opsForValue().get(username);

        if (currentVal == null) {
            logger.warn("No existing attempts found for user: {}", username);
            return false;
        }

        long attempts = Long.parseLong(currentVal);

        if (attempts > 0) {
            attempts--;
        }

        redisTemplate.opsForValue().set(username, String.valueOf(attempts));

        logger.warn("Decreased attempts for user: {} -> {}", username, attempts);

        return true;

    } catch (Exception e) {
        logger.error("Error decreasing attempts for user: {}", username, e);
        return false;
    }
}

    public boolean isNoAttempRemaining(String username) {
        try {
            String val = redisTemplate.opsForValue().get(username);

            if (val == null) {
                logger.warn("No value found for user: {}", username);
                return false;
            }

            if ("0".equals(val)) {
                logger.warn("No attempts remaining for user: {}", username);

                redisTemplate.opsForValue()
                        .set(username, "LOCKED", Duration.ofHours(2));

                logger.info("User {} locked for 2 hours", username);
                return true;
            }

            return false;

        } catch (Exception e) {
            logger.error("Error checking attempts for user: {}", username, e);
            return false;
        }
    }

    public String remaingLoginAttempts(String username) {
        try {
            String attempts = redisTemplate.opsForValue().get(username);
            logger.info("Remaining attempts for user: {} -> {}", username, attempts);
            return attempts;
        } catch (Exception e) {
            logger.error("Error fetching remaining attempts for user: {}", username, e);
            return null;
        }
    }
}