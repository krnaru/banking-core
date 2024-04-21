package org.example.corebanking.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testPing() {
        String body = this.restTemplate.getForObject("/ping", String.class);
        assertEquals("pong", body);
    }
}
