package com.san.kie.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.ServiceResponse.ResponseType;
import org.kie.server.api.model.definition.ProcessDefinition;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.ProcessInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;

import com.san.kie.serverclient.conf.DecisionServerTest;

import sandev.admission.Student;

import org.kie.server.client.ProcessServicesClient;

public class KieClient {
	DecisionServerTest dsTest;
	QueryServicesClient queryClient;
	ProcessServicesClient processClient;
	Student student;

	public void init() {
		dsTest = new DecisionServerTest();
		dsTest.initialize();
		queryClient = dsTest.getKieServicesClient().getServicesClient(QueryServicesClient.class);
		processClient = dsTest.getKieServicesClient().getServicesClient(ProcessServicesClient.class);
		student = new Student();
	}

	public static void main(String[] args) {
		KieClient client = new KieClient();
		client.init();
//		client.listCapabilities();
//		client.listContainers();
//		client.disposeContainer("admission");
		//"TestProject", "sandev", "testproj", "1.0" 		"mortgages","mortgages", "mortgages", "0.0.1"	"admission", "sandev", "Admission", "1.0" 
//		client.deployContainer("admission", "sandev", "Admission", "1.0"); 
		
//		client.listContainerProcesses("admission");
//		Long processInstanceId = client.startContainerProcess("admission", "Admission.AdmissionProcess");
//		client.startProcessTaskForUser("admission", "kieserver");
//		client.abortContainerProcess("admission", processInstanceId);
	}

	public void listCapabilities() {
		KieServerInfo serverInfo = dsTest.getKieServicesClient().getServerInfo().getResult();
		System.out.print("Server capabilities:");
		for (String capability : serverInfo.getCapabilities()) {
			System.out.print(" " + capability);
		}
	}

	public void listContainers() {
		KieContainerResourceList containers = dsTest.getKieServicesClient().listContainers().getResult();
		System.out.println("Available containers: ");
		if (null != containers) {
			List<KieContainerResource> kieContainers = containers.getContainers();
			if (null != kieContainers && !kieContainers.isEmpty()) {
				for (KieContainerResource container : kieContainers) {
					System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
				}
			}
		}
	}

	public void disposeContainer(String containerId) {
		System.out.println("== Disposing container ==");
		KieContainerResourceList containers = dsTest.getKieServicesClient().listContainers().getResult();
		if (containers != null) {
			List<KieContainerResource> kieContainers = containers.getContainers();
			if (null != kieContainers && !kieContainers.isEmpty()) {
				for (KieContainerResource kieContainerResource : kieContainers) {
					if (kieContainerResource.getContainerId().equals(containerId)) {
						ServiceResponse<Void> responseDispose = dsTest.getKieServicesClient()
								.disposeContainer(containerId);
						if (responseDispose.getType() == ResponseType.FAILURE) {
							System.out.println("Error disposing " + containerId + ". Message: ");
							System.out.println(responseDispose.getMsg());
							return;
						}
						System.out.println("Success Disposing container " + containerId);
					}
				}
			}
		}
	}

	public void deployContainer(String containerId, String groupId, String artifactId, String version) {
		System.out.println("== Deploying container ==");
		boolean deployContainer = true;
		KieContainerResourceList containers = dsTest.getKieServicesClient().listContainers().getResult();
		// check if the container is not yet deployed, if not deploy it
		if (containers != null) {
			List<KieContainerResource> kieContainers = containers.getContainers();
			if (null != kieContainers && !kieContainers.isEmpty()) {
				for (KieContainerResource kieContainerResource : kieContainers) {
					if (kieContainerResource.getContainerId().equals(containerId)) {
						System.out.println("\t######### Found container " + containerId + " skipping deployment...");
						deployContainer = false;
						break;
					}
				}
			}
		}
		// deploy container if not there yet
		if (deployContainer) {
			System.out.println("\t######### Deploying container " + containerId);
			KieContainerResource resource = new KieContainerResource(containerId,
					new ReleaseId(groupId, artifactId, version));
			dsTest.getKieServicesClient().createContainer(containerId, resource);
			System.out.println("Container " + containerId + " is deployed successfully");
		}
	}
	
	public void listContainerProcesses(String containerId) {
	    System.out.println("== Listing Business Processes For container "+containerId+" ==");  
	    queryClient = dsTest.getKieServicesClient().getServicesClient(QueryServicesClient.class);  
	    List<ProcessDefinition> findProcessesByContainerId = queryClient.findProcessesByContainerId(containerId, 0, 1000);  
	    for (ProcessDefinition def : findProcessesByContainerId) {
	        System.out.println(def.getName() + " - " + def.getId() + " v" + def.getVersion());  
//	        System.out.println("\t######### Definition details: " + def);
	    }  
	} 
	
	public Long startContainerProcess(String containerId, String processId) {
		processClient = dsTest.getKieServicesClient().getServicesClient(ProcessServicesClient.class);
		// get details of process definition
//		ProcessDefinition definition = processClient.getProcessDefinition(containerId, processId);
//		System.out.println("\t######### ProcessDefinition details: " + definition);
		
		// start process instance
		Map<String, Object> params = new HashMap<String, Object>();
		student.setName("Sandeep");
		params.put("student", student); 
//		params.put("name", "Sandeep"); 
		Long processInstanceId = processClient.startProcess(containerId, processId, params);
		System.out.println("\t######### Process instance id: " + processInstanceId);
		return processInstanceId;
	}
	
	public void startProcessTaskForUser(String containerId, String user) {
		UserTaskServicesClient taskClient = dsTest.getKieServicesClient().getServicesClient(UserTaskServicesClient.class);
		// find available tasks
		List<TaskSummary> tasks = taskClient.findTasksAssignedAsPotentialOwner(user, 0, 10);
//		List<TaskSummary> tasks = taskClient.findTasks(user, 0, 10);
		System.out.println("\t######### Tasks list size: " +tasks.size());
		// complete tasks
		for(TaskSummary task : tasks) {
			System.out.println("\t######### Task: " +task);
			taskClient.startTask(containerId, task.getId(), user);
			Map<String, Object> params = new HashMap<String, Object>();
			student.setGpa(7.6f);
			params.put("student", student); 
//			params.put("gpa", 7.6f); 
			taskClient.completeTask(containerId, task.getId(), user, params);
		}
	}
	
	public void abortContainerProcess(String containerId, long processInstanceId) {
//		ProcessInstance processInstance = queryClient.findProcessInstanceById(processInstanceId);
//		System.out.println("\t######### ProcessInstance: " + processInstance);
		
		List<NodeInstance> completedNodes = queryClient.findCompletedNodeInstances(processInstanceId, 0, 10);
		System.out.println("\t######### Completed nodes: " + completedNodes);
		
		// at the end abort process instance
		processClient.abortProcessInstance(containerId, processInstanceId);
		System.out.println("\t######### Aborted processInstanceId: " + processInstanceId);
	}
}
