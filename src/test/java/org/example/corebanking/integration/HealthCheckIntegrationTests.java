package org.example.corebanking.integration;

import org.example.corebanking.CoreBankingApplication;
import org.example.corebanking.integration.util.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = {CoreBankingApplication.class, TestContainersConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckIntegrationTests extends TestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPing() {
        String body = this.restTemplate.getForObject("/ping", String.class);
        assertEquals("pong", body);
    }
}
