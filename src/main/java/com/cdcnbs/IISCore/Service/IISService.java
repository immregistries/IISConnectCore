package com.cdcnbs.IISCore.Service;

import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.query.QueryConverter;
import org.immregistries.smm.tester.manager.query.QueryConverterQBPZ34;
import org.immregistries.smm.tester.manager.query.QueryRequest;
import org.immregistries.smm.tester.manager.query.QueryResponse;
import org.immregistries.smm.tester.manager.response.ImmunizationMessage;
import org.immregistries.smm.tester.manager.response.ResponseReader;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

import com.cdcnbs.IISCore.Data.IISData;
import com.cdcnbs.IISCore.Data.Person;
import com.cdcnbs.IISCore.Utils.Messaging;


public class IISService {

	public static IISData queryIIS(Person person) throws Exception {
		String responseMessage = "";
		IISData ret = new IISData();
		
		QueryResponse queryResponse = new QueryResponse();
	    if (person != null) {
	    	
	    	// Include the patient in the return object
	    	ret.setPatient(person);
	    	
	    	// Map patient record to query request object;
			QueryRequest queryRequest = Messaging.getQueryRequestFromPatient(person);
			
			TestCaseMessage testCaseMessage = Messaging.createTestCaseMessage(queryRequest);

	        // Request query type to get vaccine records.
			QueryConverter queryConverter = new QueryConverterQBPZ34();
	              
	        // Get HL7 representation of patient data.  
	        TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
	        queryTestCaseMessage.setMessageText(queryConverter.convert(testCaseMessage.getMessageText()));
	    
	        // Clean the text
	        String message = Messaging.cleanMessage(queryTestCaseMessage.getMessageText());
	        
	         
	        
			try {
				 
				Boolean debug = true;
				
				// Create connector from Web Service configuration information stored in person object.
				Connector connector = Connector.makeConnectors(person.getIISConfig()).get(0);
				
				// custom transformations required by state IIS obtained from the connector.
				String transforms = connector.getCustomTransformations();
				Transformer transformer = new Transformer();
				
				message = transformer.transformForSubmitServlet(connector,message, transforms, null, null, null);
			
				// Include message in returned object.
		        ret.setRequestHL7(message);  
		        
				// Get HL7 response from State IIS and include in returned object
				responseMessage = connector.submitMessage(message, debug);
				ret.setResponseHL7(responseMessage);
				
				
				// Get immunization objects
				ImmunizationMessage immunizationMessage = ResponseReader.readMessage(responseMessage);
				
				// Immunization Message includes properties for patient and patientList but they are null.
				// Discuss how to get these records returned.
				
				// get immunizations and map to Vaccination
				queryResponse = (QueryResponse) immunizationMessage;
				
				// Include the query response 
				ret.setQueryResponse(queryResponse);
				return ret;

				
			} catch (Exception e1) {
				throw new Exception(e1.getLocalizedMessage());
			}		    
	    	
	    }

	    // return empty
	    return ret;
		
		
	}
	// Overloaded method that allows passing the configuration string as an argument.
	public static IISData queryIIS(Person person, String config) throws Exception {
		
		person.setIISConfig(config);
		return queryIIS(person);
		
	}
}
