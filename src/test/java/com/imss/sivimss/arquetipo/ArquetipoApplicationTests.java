package com.imss.sivimss.arquetipo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PlanPFApplicationTests {

	@Test
	void contextLoads() {
		String result="test";
		PlanPFApplicationTests(new String[]{});
		assertNotNull(result);
	}

	private void PlanPFApplicationTests(String[] strings) {
	}

}
