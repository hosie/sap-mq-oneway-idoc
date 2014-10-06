package com.your.company.domain.IdocPassThrough.code;

import com.ibm.broker.config.appdev.patterns.GeneratePatternInstanceTransform;
import com.ibm.broker.config.appdev.patterns.PatternInstanceManager;
import com.ibm.broker.config.appdev.MessageFlow;

public class PatternGenerator implements GeneratePatternInstanceTransform {
	
	private final static String PROJECT_NAME = "IdocPassThroughFlow";
	
	/** Properties **/
	private final static String PROPERTY_LOGGING_REQUIRED = "pp5";
	private final static String PROPERTY_ERROR_HANDLING_REQUIRED = "pp6";
	
	/** Flows **/
	private final static String FLOW_IDOC = "mqsi/IDOCPassThroughFlow.msgflow";
	private final static String FLOW_LOG = "mqsi/Log.msgflow";
	private final static String FLOW_ERROR = "mqsi/Error.msgflow";
	
	/** Nodes **/
	private final static String NODE_LOG = "Log";
	private final static String NODE_ERROR = "Error";
	
	@Override
	public void onGeneratePatternInstance(PatternInstanceManager patternInstanceManager) {
		
		// The location for the generated projects 
		String location = patternInstanceManager.getWorkspaceLocation();
		
		// The pattern instance name for this generation
		String patternInstanceName = patternInstanceManager.getPatternInstanceName();
		
		MessageFlow idocMessageFlow = patternInstanceManager.getMessageFlow(PROJECT_NAME, FLOW_IDOC);
		MessageFlow logMessageFlow = patternInstanceManager.getMessageFlow(PROJECT_NAME, FLOW_LOG);
		MessageFlow errorMessageFlow = patternInstanceManager.getMessageFlow(PROJECT_NAME, FLOW_ERROR);
		
		boolean loggingRequired = patternInstanceManager.getParameterValue(PROPERTY_LOGGING_REQUIRED).equals("true");
		if (!loggingRequired) {
			patternInstanceManager.removeMessageFlow(logMessageFlow);
				if (idocMessageFlow != null) {
					idocMessageFlow.removeNode(idocMessageFlow.getNodeByName(NODE_LOG));
				}
		}

		boolean errorRequired = patternInstanceManager.getParameterValue(PROPERTY_ERROR_HANDLING_REQUIRED).equals("true");
		if (!errorRequired) {
				patternInstanceManager.removeMessageFlow(errorMessageFlow);
				if (idocMessageFlow != null) {
					idocMessageFlow.removeNode(idocMessageFlow.getNodeByName(NODE_ERROR));
				}
		}
	}
}
