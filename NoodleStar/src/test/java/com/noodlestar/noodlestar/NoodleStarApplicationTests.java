package com.noodlestar.noodlestar;

import com.noodlestar.noodlestar.auth0.Auth0WebClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {Auth0WebClient.class})
class NoodleStarApplicationTests {


	//@Test
	void contextLoads() {
	}

}
