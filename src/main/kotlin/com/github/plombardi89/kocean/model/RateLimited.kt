package com.github.plombardi89.kocean.model

import java.time.Duration
import java.time.Instant


public class RateLimited(val resetTime: Instant):
    RuntimeException(
        "API rate limit exceeded (reset: ${resetTime}, delta: ${Duration.between(Instant.now(), resetTime)})")