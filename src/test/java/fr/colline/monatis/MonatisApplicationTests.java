package fr.colline.monatis;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MonatisApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@Test
	void utiliseLaBaseH2DeTest() throws SQLException {

		try (Connection connection = dataSource.getConnection()) {
			String url = connection.getMetaData().getURL();

			assertTrue(
					url.startsWith("jdbc:h2:mem:monatis-test"),
					"Les tests doivent utiliser la base H2 dediee, url actuelle : " + url);
		}
	}
}
