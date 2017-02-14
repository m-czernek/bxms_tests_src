package getting_started_guide;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.brms.myproject.Person;
import org.drools.core.command.impl.GenericCommand;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.ServiceResponse.ResponseType;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import utils.AbstractJUnitTest;

import org.kie.server.api.model.ReleaseId;

import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;


public class SimpleProcessJUnitTest extends AbstractJUnitTest {
	
	public SimpleProcessJUnitTest() throws MalformedURLException {
		super();
	}
	
	@Test
	public void Init() throws IOException{
		//declare post request flags
		Map<String,String> flags = new HashMap<String,String>();
		flags.put("Accept", "application/json");
		flags.put("Content-Type", "application/json");
		
		//Clone the repository
		assertTrue(cloneRepository(flags).equals("APPROVED"));
		
		String repoName = "clonedRepository";
		String projectName = "myProject";
		
		//build and deploy
		setClientURL("repositories/"+repoName+"/projects/"+projectName+"/maven/deploy");
		String response = client.PostRequest(null, flags);
		System.out.println(response);
		
		//TODO:test BUILD & DEPLOY
		//TODO:create container
		//TODO:actual tests
		//TODO:cleanup()
	}
	
	//@Test
	public void simpleProcessTest() {

		Person p1 = new Person();
		p1.setFirstName("Anton");
		p1.setLastName("RedHat");
		p1.setHourlyRate(11);
		p1.setWage(20);
		p1.setIsRich(false);
		
		String url = "http://localhost:8080/kie-server/services/rest/server";
	    String username = "kieserver";
	    String password = "kieserver1!";
	    String container = "myJavaContainer";
	    String session = "mySession";
	    
	    KieServicesConfiguration config = KieServicesFactory.newRestConfiguration(url, username, password);
	    
	    assertNotNull(config);
	    
	    
	    Set<Class<?>> allClasses = new HashSet<Class<?>>();
	    allClasses.add(Person.class);
	    config.addExtraClasses(allClasses);

	    KieServicesClient client  = KieServicesFactory.newKieServicesClient(config);
	    assertNotNull(client);
	    
	    
	    RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
	    assertNotNull(ruleClient);
	    
	    
	    List<GenericCommand<?>> commands = new ArrayList<GenericCommand<?>>();
	    commands.add((GenericCommand<?>) KieServices.Factory.get().getCommands().newInsert(p1,"Person Insert ID"));
	    commands.add((GenericCommand<?>) KieServices.Factory.get().getCommands().newFireAllRules("fire-identifier"));

	    BatchExecutionCommand batchCommand = KieServices.Factory.get().getCommands().newBatchExecution(commands,session);
	    ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(container, batchCommand);
	    assertEquals("SUCCESS", response.getType().toString());		
	}
	
	//@Test
//	public void createContainer(){
//		KieServicesConfiguration config =  KieServicesFactory.newRestConfiguration("http://localhost:8080/kie-server/services/rest/server",
//		        user,
//		        password);
//		KieServicesClient client = KieServicesFactory.newKieServicesClient(config);
//
//		List<KieContainerResource> kieContainers = client.listContainers().getResult().getContainers();
//		if (kieContainers.size() == 0) {  
//	        System.out.println("No containers available...");  
//	        return;  
//	    }
//		
//		for(KieContainerResource i : kieContainers){
//			System.out.println(i.getContainerId()+":"+i.getReleaseId()+";"+i.getStatus());
//		}
		
//		ServiceResponse<KieContainerResource> createResponse = client.createContainer("myJavaContainer",  new KieContainerResource( new ReleaseId("org.brms","MyProject","1.0.3") ));
//		if(createResponse.getType() == ResponseType.FAILURE) {  
//	        System.out.println("Error creating myJavaContainer: ");
//	        System.out.println(createResponse.getMsg());
//	        return;  
//	    }  
//	     System.out.println("Container recreated with success!");  
//	}
	
	private String cloneRepository(Map<String,String>flags){
		setClientURL("repositories/");	
		
		String json = "{\"name\":\"clonedRepository\",\"description\":null,\"requestType\":\"clone\",\"gitURL\":\"https://github.com/m-czernek/BxMSTests.git\",\"organizationalUnitName\":\"example\"}\n";

		String response = null;
		try {
			response = client.PostRequest(json, flags);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		resetClientURL();
		String returnCode = response.split(",")[1].split(":")[1];
		returnCode = returnCode.substring(1, returnCode.length() - 1);
		return returnCode;
		
	}
}
