package com.stockBridge.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.flyway.enabled=false",
		"spring.jpa.hibernate.ddl-auto=none",
		"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
		"spring.datasource.url=jdbc:postgresql://localhost:5432/mockdb"
})
class InventoryApplicationTests {

	@Test
	void contextLoads() {
	}

}
