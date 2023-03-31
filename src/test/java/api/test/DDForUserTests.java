package api.test;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.playload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DDForUserTests {
	
	@Test(priority=1, dataProvider="Data", dataProviderClass= DataProviders.class)
	public void createUserTest(String userID, String userName, String fname,String lname, String email, String pw, String pno ) {
		
		User userPlayload = new User();
		userPlayload.setId(Integer.parseInt(userID));
		userPlayload.setUsername(userName);
		userPlayload.setFirstName(fname);
		userPlayload.setLastName(lname);
		userPlayload.setEmail(email);
		userPlayload.setPassword(pw);
		userPlayload.setPhone(pno);
		
		Response  response = UserEndPoints.createUser(userPlayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=1, dataProvider="UserNames", dataProviderClass=DataProviders.class)
	public void deleteUserTest(String userName) {
		
		 Response  response =UserEndPoints.deleteUser(userName);
		 response.then().log().all();
		 Assert.assertEquals(response.getStatusCode(), 200);
	}

}
