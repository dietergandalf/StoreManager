package com.dietergandalf.store_manager;

import com.dietergandalf.store_manager.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@EnableAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class,
    OAuth2ClientWebSecurityAutoConfiguration.class,
    Saml2RelyingPartyAutoConfiguration.class,
    OAuth2ResourceServerAutoConfiguration.class,
    OAuth2ClientAutoConfiguration.class
})
class StoreManagerApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context loads successfully
	}

}
