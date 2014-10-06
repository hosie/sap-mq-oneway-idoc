package mqsi;

import java.util.List;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.*;

public class IDOCPassThroughFlow_JavaCompute extends MbJavaComputeNode {

	// XPAth objects used to access the tree, create here to avoid them being created per message
	private MbXPath rawIdocXpath;
	private MbXPath idocTypeXpath;
	private MbXPath destinationListXpath;
	
	public IDOCPassThroughFlow_JavaCompute() throws MbException {
		super();		
		rawIdocXpath = new MbXPath("ns:SapGenericIDocObject/IDocStreamData");
		rawIdocXpath.addNamespacePrefix("ns", "http://www.ibm.com/xmlns/prod/websphere/j2ca/sap/sapgenericidocobject");
		idocTypeXpath = new MbXPath("ns:SapGenericIDocObject/IDocType");
		idocTypeXpath.addNamespacePrefix("ns", "http://www.ibm.com/xmlns/prod/websphere/j2ca/sap/sapgenericidocobject");
		destinationListXpath = new MbXPath("?Destination/?MQ/?DestinationData/?queueName");	
	}
	
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		
		// create new message
		MbMessage outMessage = new MbMessage();
		// create a new local environment
		MbMessage outputLocalEnv = new MbMessage();

		try {
			// ----------------------------------------------------------
			// Add user code below
			
			// SET OutputRoot.BLOB.BLOB = InputRoot.DataObject.ns:SapGenericIDocObject.IDocStreamData;
			// Copy the raw Idoc to the BLOB domain			 
			MbElement root = inMessage.getRootElement().getLastChild();
			
			MbElement rawIdocelement = this.evaluateXpathAndValidate(rawIdocXpath, root);
			MbElement outParser = outMessage.getRootElement().createElementAsLastChild(MbBLOB.PARSER_NAME);
			outParser.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "BLOB", rawIdocelement.getValue());
			
			// SET OutputLocalEnvironment.Destination.MQ.DestinationData[1].queueName = InputRoot.DataObject.ns:SapGenericIDocObject.IDocType;
			// Set the destination name	
			MbElement iDocTypeElement = this.evaluateXpathAndValidate(idocTypeXpath, root);
			MbElement  queueName = this.evaluateXpathAndValidate(destinationListXpath, outputLocalEnv.getRootElement());
			queueName.setValue(iDocTypeElement.getValue());
			
			// create the output message assembly
			MbMessageAssembly outAssembly = new MbMessageAssembly(inAssembly, outputLocalEnv, inAssembly.getExceptionList(), outMessage);
			
			// End of user code
			// ----------------------------------------------------------

			// The following should only be changed
			// if not propagating message to the 'out' terminal
			out.propagate(outAssembly);

		} finally {
			// clear the outMessage
			outMessage.clearMessage();
		}
	}
	
	private MbElement evaluateXpathAndValidate(MbXPath xpath, MbElement root) throws MbException {
		Object object = root.evaluateXPath(xpath);
		if (object instanceof List) {
			List list = (List)object;
			if (list.size() > 0) {
				Object element = list.get(0);
				return ((element instanceof MbElement) ? ((MbElement) element) : null);
			}
		}
		return null;		
	}

}
