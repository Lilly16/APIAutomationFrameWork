package api.test;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import com.github.javafaker.Faker;
import api.endpoints.UserEndPoints;
import api.playload.User;
import io.restassured.response.Response;

public class UserTests {
	
	// Here Faker library is used for "generating the data"
	Faker faker;
	User userPlayload;
	//public org.apache.logging.log4j.Logger logger;
	
	@BeforeClass
	public void setUp() {
		
		faker = new Faker();
		userPlayload= new User();
		
		userPlayload.setId(faker.idNumber().hashCode());
		userPlayload.setUsername(faker.name().username());
		userPlayload.setFirstName(faker.name().firstName());
		userPlayload.setLastName(faker.name().lastName());
		userPlayload.setEmail(faker.internet().safeEmailAddress());
		userPlayload.setPassword(faker.internet().password(5, 10));
		userPlayload.setPhone(faker.phoneNumber().cellPhone());
	
		//logs
		//logger = LogManager.getLogger(this.getClass());
	
	}
	
	@Test(priority=1)
	public void createUserTest() {
		
		//logger.info("************ creating user ***************");
		Response createUserResponse = UserEndPoints.createUser(userPlayload);
		createUserResponse.then().log().all();
		Assert.assertEquals(createUserResponse.getStatusCode(), 200);
	
		//logger.info("******************* User created *******************");
	}
	
	@Test(priority=2,dependsOnMethods="createUserTest")
	public void getUserTest() {
		Response getUserResponse = UserEndPoints.getUser(this.userPlayload.getUsername());
		getUserResponse.then().log().all();
		Assert.assertEquals(getUserResponse.getStatusCode(), 200);
	}
	
	@Test(priority=3,dependsOnMethods="getUserTest")
	public void updateUserTest() {
		
		userPlayload.setFirstName(faker.name().firstName());
		userPlayload.setLastName(faker.name().lastName());
		userPlayload.setEmail(faker.internet().safeEmailAddress());
		
		Response updateUserResponse = UserEndPoints.updateUser(this.userPlayload.getUsername(), userPlayload);
		updateUserResponse.then().log().body();
		Assert.assertEquals(updateUserResponse.getStatusCode(), 200);
		
		//Checking data after update:
		
		Response getUserResponse = UserEndPoints.getUser(this.userPlayload.getUsername());
		Assert.assertEquals(getUserResponse.getStatusCode(), 200);
	}
	@Test(priority=4,dependsOnMethods="updateUserTest")
	public void deleteUser() {
	Response deleteUserResponse = UserEndPoints.deleteUser(this.userPlayload.getUsername());
	
	Assert.assertEquals(deleteUserResponse.getStatusCode(), 200);
	}
	

}
