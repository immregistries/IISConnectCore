package com.cdcnbs.IISCore.Data;

import org.immregistries.smm.tester.manager.query.QueryResponse;

public class IISData {
	private String RequestHL7;	
	private String ResponseHL7;	
	private QueryResponse QueryResponse;	
	private Person Person;
	public String getRequestHL7() {
		return RequestHL7;
	}
	public void setRequestHL7(String requestHL7) {
		RequestHL7 = requestHL7;
	}
	public String getResponseHL7() {
		return ResponseHL7;
	}
	public void setResponseHL7(String responseHL7) {
		ResponseHL7 = responseHL7;
	}
	public QueryResponse getQueryResponse() {
		return QueryResponse;
	}
	public void setQueryResponse(QueryResponse queryResponse) {
		QueryResponse = queryResponse;
	}
	public Person getPatient() {
		return Person;
	}
	public void setPatient(Person patient) {
		Person = patient;
	}
}
