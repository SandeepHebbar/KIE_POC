package com.san.kie.serverclient.conf;

import org.kie.server.api.marshalling.MarshallingFormat;  
import org.kie.server.client.KieServicesClient;  
import org.kie.server.client.KieServicesConfiguration;  
import org.kie.server.client.KieServicesFactory;  

public class DecisionServerTest {
	private static final String URL = "http://localhost:8080/kie-server/services/rest/server";  
    private static final String USER = "kieserver";  
    private static final String PASSWORD = "kieserver1!"; 
  
    private static final MarshallingFormat FORMAT = MarshallingFormat.XSTREAM; //JSON	XSTREAM
  
    private KieServicesConfiguration conf;  
    private KieServicesClient kieServicesClient;  
    
    public void initialize() {  
        conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);  
        conf.setMarshallingFormat(FORMAT);  
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);  
    }

	public KieServicesClient getKieServicesClient() {
		return kieServicesClient;
	}

	public void setKieServicesClient(KieServicesClient kieServicesClient) {
		this.kieServicesClient = kieServicesClient;
	}  
}
