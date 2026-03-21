package fr.colline.monatis;

import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.boot.test.context.SpringBootTest;

import fr.colline.monatis.service.BanqueServiceTests;
import fr.colline.monatis.service.CategorieServiceTests;
import fr.colline.monatis.service.CompteTiersServiceTests;

@SpringBootTest
public class MonatisApplicationTests {

	@Test
	void serviceTests()
	{
		Result result = JUnitCore.runClasses(
				BanqueServiceTests.class,
				CategorieServiceTests.class,
				CompteTiersServiceTests.class);
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println(result.wasSuccessful());
	}
}
