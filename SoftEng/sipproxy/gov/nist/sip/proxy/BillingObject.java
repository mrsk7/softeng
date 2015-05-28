package gov.nist.sip.proxy;


public class BillingObject {
	
	protected ConnectionObject connObj = null;
	
	public BillingObject(ConnectionObject connObj) { 
		this.connObj = connObj;
	}

	
	public double calculateCost(String policy,long duration) {
		double cost = -1;
		double init = connObj.getInitCost(policy);
		double costPerSec = connObj.getCostPer(policy);
		cost = init + duration * costPerSec;
		return cost;
	}

}
