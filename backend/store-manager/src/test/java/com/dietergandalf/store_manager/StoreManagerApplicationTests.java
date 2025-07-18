package com.dietergandalf.store_manager;

import com.dietergandalf.store_manager.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class StoreManagerApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context loads successfully
	}

}
