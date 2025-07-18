package com.dietergandalf.store_manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {
    // Test configuration - security disabled via application-test.properties
}
